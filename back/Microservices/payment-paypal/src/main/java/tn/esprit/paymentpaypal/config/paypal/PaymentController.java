package tn.esprit.paymentpaypal.config.paypal;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class PaymentController {
    private final PaypalService paypalService;
    private final PaymentHistoryService paymentHistoryService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            Payment payment = paypalService.createPayment(
                    paymentRequest.getAmount(),
                    paymentRequest.getCurrency(),
                    "paypal",
                    "sale",
                    paymentRequest.getDescription(),
                    paymentRequest.getCancelUrl(),
                    paymentRequest.getSuccessUrl()
            );

            PaymentResponse response = new PaymentResponse();
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    response.setApprovalUrl(link.getHref());
                    response.setPaymentId(payment.getId());

                    Paiement paiement = new Paiement(
                            paymentRequest.getAmount(),
                            paymentRequest.getCurrency(),
                            "paypal",
                            new Date(),
                            payment.getId()
                    );
                    paymentHistoryService.savePayment(paiement);

                    return ResponseEntity.ok(response);
                }
            }
            response.setErrorMessage("Failed to create payment");
            return ResponseEntity.badRequest().body(response);
        } catch (PayPalRESTException e) {
            log.error("Error occurred: " + e.getMessage());
            PaymentResponse response = new PaymentResponse();
            response.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PostMapping("/execute")
    public ResponseEntity<String> executePayment(@RequestBody PaymentExecutionRequest executionRequest) {
        try {
            Payment payment = paypalService.executePayment(executionRequest.getPaymentId(), executionRequest.getPayerId());
            if ("approved".equals(payment.getState())) {
                Paiement paiement = paymentHistoryService.findByPaymentId(executionRequest.getPaymentId());
                if (paiement != null) {
                    paiement.setStatus(PaymentStatus.APPROVED);
                    paiement.setDate(new Date());
                    paymentHistoryService.savePayment(paiement);
                }
                return ResponseEntity.ok("Payment successful :" + payment.getId());
            }
            return ResponseEntity.badRequest().body("Payment not approved :" + payment.getId());
        } catch (PayPalRESTException e) {
            log.error("Error occurred: " + e.getMessage());
            Paiement paiement = paymentHistoryService.findByPaymentId(executionRequest.getPaymentId());
            if (paiement != null) {
                paiement.setStatus(PaymentStatus.FAILED);
                paymentHistoryService.savePayment(paiement);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error executing payment: " + e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        Paiement lastPayment = paymentHistoryService.getAllPayments().stream()
                .max(Comparator.comparing(Paiement::getDate))
                .orElse(null);
        if (lastPayment != null) {
            lastPayment.setStatus(PaymentStatus.CANCELED);
            paymentHistoryService.savePayment(lastPayment);
        }
        return ResponseEntity.ok("Payment canceled");
    }

    @GetMapping("/error")
    public ResponseEntity<String> paymentError() {
        Paiement lastPayment = paymentHistoryService.getAllPayments().stream()
                .max(Comparator.comparing(Paiement::getDate))
                .orElse(null);
        if (lastPayment != null) {
            lastPayment.setStatus(PaymentStatus.FAILED);
            paymentHistoryService.savePayment(lastPayment);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment error occurred");
    }

    @GetMapping("/history")
    public ResponseEntity<List<Paiement>> getPaymentHistory() {
        return ResponseEntity.ok(paymentHistoryService.getAllPayments());
    }

    @GetMapping("/pdf/{paymentId}")
    public ResponseEntity<byte[]> generatePaymentPdf(@PathVariable String paymentId) {
        try {
            Paiement paiement = paymentHistoryService.findByPaymentId(paymentId);
            if (paiement == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Payment not found".getBytes());
            }

            // Generate PDF
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Define custom color (#8121FB)
            DeviceRgb customColor = new DeviceRgb(129, 33, 251);

            // Header
            Paragraph header = new Paragraph("Payment Receipt")
                    .setFontSize(20)
                    .setBold()
                    .setFontColor(customColor)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(header);

            // Add some spacing
            document.add(new Paragraph("\n"));

            // Payment Details Table
            Table table = new Table(UnitValue.createPercentArray(new float[]{30, 70})).useAllAvailableWidth();
            table.addHeaderCell(new Cell().add(new Paragraph("Field").setFontColor(ColorConstants.WHITE).setBackgroundColor(customColor)));
            table.addHeaderCell(new Cell().add(new Paragraph("Value").setFontColor(ColorConstants.WHITE).setBackgroundColor(customColor)));

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            table.addCell(new Cell().add(new Paragraph("Payment ID")));
            table.addCell(new Cell().add(new Paragraph(paiement.getPaymentId())));
            table.addCell(new Cell().add(new Paragraph("Amount")));
            table.addCell(new Cell().add(new Paragraph(paiement.getAmount() + " " + paiement.getCurrency())));
            table.addCell(new Cell().add(new Paragraph("Payment Method")));
            table.addCell(new Cell().add(new Paragraph(paiement.getPaymentMethod())));
            table.addCell(new Cell().add(new Paragraph("Date")));
            table.addCell(new Cell().add(new Paragraph(dateFormat.format(paiement.getDate()))));
            table.addCell(new Cell().add(new Paragraph("Status")));
            table.addCell(new Cell().add(new Paragraph(paiement.getStatus().toString())));

            document.add(table);

            // Footer
            Paragraph footer = new Paragraph("Generated on: " + dateFormat.format(new Date()))
                    .setFontSize(10)
                    .setFontColor(customColor)
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(new Paragraph("\n"));
            document.add(footer);

            document.close();

            // Set response headers for PDF download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "payment_" + paymentId + ".pdf");

            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error generating PDF: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating PDF".getBytes());
        }
    }
}