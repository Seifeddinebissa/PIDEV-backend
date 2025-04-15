package tn.esprit.gestionformation.Services;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tn.esprit.gestionformation.Config.EmailService;
import tn.esprit.gestionformation.Entities.Feedback;
import tn.esprit.gestionformation.Entities.Formation;
import tn.esprit.gestionformation.Repositories.FeedbackRepository;
import tn.esprit.gestionformation.Repositories.FormationRepository;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private EmailService emailService;

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(int id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public Feedback saveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(int id) {
        feedbackRepository.deleteById(id);
    }

    public Feedback updateFeedback(int id, Feedback updatedFeedback) {
        return feedbackRepository.findById(id).map(feedback -> {
            feedback.setRating(updatedFeedback.getRating());
            feedback.setComment(updatedFeedback.getComment());
            feedback.setDate(updatedFeedback.getDate());
            feedback.setIs_hidden(updatedFeedback.isIs_hidden());
            return feedbackRepository.save(feedback);
        }).orElse(null);
    }

    public List<Feedback> getNonHiddenFeedbacksByFormationId(int formationId) {
        return feedbackRepository.findNonHiddenByFormationId(formationId);
    }

    public List<Map<String, Object>> getAllFormationsFeedbackStats() {
        try {
            List<Formation> formations = formationRepository.findAll();
            if (formations.isEmpty()) {
                System.out.println("Aucune formation trouvée.");
                return Collections.emptyList();
            }

            // Calculer les stats pour chaque formation
            List<Map<String, Object>> allStats = formations.stream().map(formation -> {
                List<Feedback> feedbacks = feedbackRepository.findNonHiddenByFormationId(formation.getId());
                Map<String, Object> stats = new HashMap<>();

                stats.put("formationId", formation.getId());
                stats.put("title", formation.getTitle() != null ? formation.getTitle() : "Titre inconnu");
                stats.put("image", formation.getImage() != null ? formation.getImage() : "assets/img/courses/default.jpg");

                if (feedbacks == null || feedbacks.isEmpty()) {
                    stats.put("average", "0.00");
                    stats.put("stdDev", "0.00");
                    stats.put("totalFeedbacks", 0);
                    stats.put("distribution", Collections.emptyMap());
                } else {
                    double average = feedbacks.stream()
                            .mapToInt(Feedback::getRating)
                            .average()
                            .orElse(0.0);

                    double stdDev = Math.sqrt(feedbacks.stream()
                            .mapToDouble(f -> Math.pow(f.getRating() - average, 2))
                            .average()
                            .orElse(0.0));

                    Map<Integer, Long> distribution = feedbacks.stream()
                            .collect(Collectors.groupingBy(Feedback::getRating, Collectors.counting()));

                    stats.put("average", String.format("%.2f", average));
                    stats.put("stdDev", String.format("%.2f", stdDev));
                    stats.put("totalFeedbacks", feedbacks.size());
                    stats.put("distribution", distribution);
                }
                return stats;
            }).collect(Collectors.toList());

            // Filtrer pour ne garder que les formations avec totalFeedbacks > 0
            return allStats.stream()
                    .filter(stat -> (int) stat.get("totalFeedbacks") > 0)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erreur dans getAllFormationsFeedbackStats : " + e.getMessage());
            throw new RuntimeException("Erreur lors du calcul des stats", e);
        }
    }

    // New method to send email
    public void sendFeedbackEmail(Feedback feedback, Formation formation, int formationId) throws MessagingException {
        String subject = "Nouveau commentaire sur " + formation.getTitle();

        // Template HTML
        String htmlContent = String.format(
                """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <meta http-equiv="X-UA-Compatible" content="IE=edge">
                    <title>Nouveau Feedback</title>
                    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet">
                    <style>
                        body {
                            margin: 0;
                            padding: 0;
                            font-family: 'Poppins', 'Arial', sans-serif;
                            background-color: #f4f4f4;
                            line-height: 1.6;
                        }
                        table {
                            border-collapse: collapse;
                            width: 100%%;
                        }
                        img {
                            border: 0;
                            display: block;
                            max-width: 100%%;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            background-color: #ffffff;
                        }
                        .header {
                            background: linear-gradient(90deg, #7747ff 0%%, #9b6bff 100%%);
                            padding: 30px 20px;
                            text-align: center;
                        }
                        .header img {
                            margin: 0 auto;
                            width: 120px;
                        }
                        .header h1 {
                            color: #ffffff;
                            font-size: 24px;
                            margin: 15px 0 0;
                            font-weight: 600;
                        }
                        .content {
                            padding: 30px 20px;
                            color: #333333;
                        }
                        .content h2 {
                            font-size: 20px;
                            color: #7747ff;
                            margin: 0 0 20px;
                            font-weight: 600;
                        }
                        .content p {
                            font-size: 16px;
                            margin: 10px 0;
                            display: flex;
                            align-items: center;
                        }
                        .content img.icon {
                            width: 20px;
                            margin-right: 10px;
                        }
                        .content .highlight {
                            font-weight: 600;
                            color: #7747ff;
                        }
                        .cta-button {
                            text-align: center;
                            margin: 20px 0;
                        }
                        .cta-button a {
                            display: inline-block;
                            padding: 12px 30px;
                            background-color: #7747ff;
                            color: #ffffff;
                            text-decoration: none;
                            font-size: 16px;
                            font-weight: 600;
                            border-radius: 5px;
                        }
                        .footer {
                            background-color: #f4f4f4;
                            padding: 20px;
                            text-align: center;
                            font-size: 14px;
                            color: #777777;
                        }
                        .footer p {
                            margin: 0;
                        }
                        .footer .social-icons img {
                            width: 24px;
                            margin: 10px 5px;
                        }
                        @media only screen and (max-width: 600px) {
                            .container {
                                width: 100%% !important;
                            }
                            .header h1 {
                                font-size: 20px;
                            }
                            .content {
                                padding: 20px;
                            }
                            .content h2 {
                                font-size: 18px;
                            }
                            .content p {
                                font-size: 14px;
                            }
                            .cta-button a {
                                font-size: 14px;
                                padding: 10px 20px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <table class="container" cellpadding="0" cellspacing="0">
                        <tr>
                            <td class="header">
                                <h1>Nouveau Feedback Reçu</h1>
                            </td>
                        </tr>
                        <tr>
                            <td class="content">
                                <h2>Feedback sur la formation : %s</h2>
                                <p>
                                    <img src="https://cdn-icons-png.flaticon.com/512/1828/1828884.png" alt="Star Icon" class="icon">
                                    <span class="highlight">Note :</span> %d/5
                                </p>
                                <p>
                                    <img src="https://cdn-icons-png.flaticon.com/512/3178/3178158.png" alt="Comment Icon" class="icon">
                                    <span class="highlight">Commentaire :</span> %s
                                </p>
                                <p>
                                    <img src="https://cdn-icons-png.flaticon.com/512/2695/2695971.png" alt="Date Icon" class="icon">
                                    <span class="highlight">Date :</span> %s
                                </p>
                                <div class="cta-button">
                                    <a href="http://localhost:4200/course-details/%d">Voir les détails de la Formation</a>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="footer">
                                <p>© 2025 Coding Factory. Tous droits réservés.</p>
                                <div class="social-icons">
                                    <a href="https://facebook.com"><img src="https://cdn-icons-png.flaticon.com/512/733/733547.png" alt="Facebook"></a>
                                    <a href="https://twitter.com"><img src="https://cdn-icons-png.flaticon.com/512/733/733579.png" alt="Twitter"></a>
                                    <a href="https://linkedin.com"><img src="https://cdn-icons-png.flaticon.com/512/733/733561.png" alt="LinkedIn"></a>
                                </div>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """,
                escapeHtml(formation.getTitle()),
                feedback.getRating(),
                escapeHtml(feedback.getComment()),
                feedback.getDate().toString(),
                formationId
        );

        emailService.sendEmail("bennarimohamed8@gmail.com", subject, htmlContent);
    }

    // New method to generate PDF
    public byte[] generateFeedbackStatsPDF(List<Map<String, Object>> stats) throws Exception {
        // Créer un document PDF avec des marges personnalisées
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);
        document.setMargins(40, 40, 40, 40); // Marges aérées

        // Définir les couleurs
        Color primaryColor = new DeviceRgb(87, 81, 225); // rgb(87, 81, 225)
        Color secondaryColor = new DeviceRgb(233, 231, 255); // rgb(233, 231, 255)
        Color textColor = new DeviceRgb(50, 50, 50); // Gris foncé pour le texte

        // Charger une police personnalisée (optionnel)
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Ajouter un en-tête avec logo et titre
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{20, 50, 30}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        // Logo (à gauche)
        ClassPathResource logoResource = new ClassPathResource("static/uploads/images/logo.jpg");
        Image logo = new Image(ImageDataFactory.create(logoResource.getURL()))
                .setWidth(50)
                .setHeight(50);
        headerTable.addCell(new Cell()
                .add(logo)
                .setBorder(null)
                .setVerticalAlignment(VerticalAlignment.MIDDLE));

        // Titre principal (au centre)
        headerTable.addCell(new Cell()
                .add(new Paragraph("Statistiques des Feedbacks par Formation")
                        .setFont(fontBold)
                        .setFontSize(20)
                        .setFontColor(primaryColor))
                .setBorder(null)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.LEFT));

        // "Coding Factory" (à droite)
        headerTable.addCell(new Cell()
                .add(new Paragraph("Coding Factory")
                        .setFont(fontBold)
                        .setFontSize(16)
                        .setFontColor(primaryColor))
                .setBorder(null)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                .setTextAlignment(TextAlignment.RIGHT));

        document.add(headerTable);

        // Ajouter une ligne de séparation sous l'en-tête
        document.add(new Paragraph("")
                .setBorderBottom(new SolidBorder(primaryColor, 2))
                .setMarginTop(10)
                .setMarginBottom(20));

        // Créer un tableau pour les statistiques
        Table table = new Table(UnitValue.createPercentArray(new float[]{2, 1, 1, 1, 3}));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, 1)); // Bordure extérieure élégante
        table.setMarginTop(20);

        // En-tête du tableau avec style
        table.addHeaderCell(new Cell()
                .add(new Paragraph("Formation")
                        .setFont(fontBold)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT))
                .setBackgroundColor(primaryColor)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(12)
                .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 1)));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("★ Moyenne") // Ajout d'une icône (étoile)
                        .setFont(fontBold)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(primaryColor)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(12)
                .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 1)));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("σ Écart-type") // Ajout d'une icône (sigma)
                        .setFont(fontBold)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(primaryColor)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(12)
                .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 1)));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("∑ Total") // Ajout d'une icône (somme)
                        .setFont(fontBold)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBackgroundColor(primaryColor)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(12)
                .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 1)));
        table.addHeaderCell(new Cell()
                .add(new Paragraph("📊 Distribution") // Ajout d'une icône (graphique)
                        .setFont(fontBold)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.LEFT))
                .setBackgroundColor(primaryColor)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(12)
                .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 1)));

        // Remplir le tableau avec les données
        for (Map<String, Object> stat : stats) {
            Color rowColor = table.getNumberOfRows() % 2 == 0 ? secondaryColor : ColorConstants.WHITE;

            table.addCell(new Cell()
                    .add(new Paragraph(stat.get("title").toString())
                            .setFont(font)
                            .setFontSize(10)
                            .setFontColor(textColor)
                            .setTextAlignment(TextAlignment.LEFT))
                    .setBackgroundColor(rowColor)
                    .setPadding(12)
                    .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
            table.addCell(new Cell()
                    .add(new Paragraph(stat.get("average").toString())
                            .setFont(font)
                            .setFontSize(10)
                            .setFontColor(textColor)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBackgroundColor(rowColor)
                    .setPadding(12)
                    .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
            table.addCell(new Cell()
                    .add(new Paragraph(stat.get("stdDev").toString())
                            .setFont(font)
                            .setFontSize(10)
                            .setFontColor(textColor)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBackgroundColor(rowColor)
                    .setPadding(12)
                    .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
            table.addCell(new Cell()
                    .add(new Paragraph(stat.get("totalFeedbacks").toString())
                            .setFont(font)
                            .setFontSize(10)
                            .setFontColor(textColor)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBackgroundColor(rowColor)
                    .setPadding(12)
                    .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
            Map<Integer, Long> distribution = (Map<Integer, Long>) stat.get("distribution");
            String distStr = distribution.entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining(", "));
            table.addCell(new Cell()
                    .add(new Paragraph(distStr)
                            .setFont(font)
                            .setFontSize(10)
                            .setFontColor(textColor)
                            .setTextAlignment(TextAlignment.LEFT))
                    .setBackgroundColor(rowColor)
                    .setPadding(12)
                    .setBorderBottom(new SolidBorder(ColorConstants.LIGHT_GRAY, 0.5f)));
        }

        document.add(table);

        // Ajouter un footer avec une mise en page moderne
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, event -> {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfDocument pdfDoc = docEvent.getDocument();
            com.itextpdf.kernel.pdf.PdfPage page = docEvent.getPage();
            com.itextpdf.kernel.geom.Rectangle pageSize = page.getPageSize();

            com.itextpdf.kernel.pdf.canvas.PdfCanvas pdfCanvas = new com.itextpdf.kernel.pdf.canvas.PdfCanvas(page);
            Canvas canvas = new Canvas(pdfCanvas, pageSize);

            // Ligne de séparation au-dessus du footer
            pdfCanvas.setStrokeColor(primaryColor)
                    .setLineWidth(1)
                    .moveTo(40, 60)
                    .lineTo(pageSize.getWidth() - 40, 60)
                    .stroke();

            // Footer avec mise en page en colonnes
            Table footerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}));
            footerTable.setWidth(UnitValue.createPercentValue(100));
            footerTable.setMarginTop(10);

            footerTable.addCell(new Cell()
                    .add(new Paragraph("Coding Factory\nGestion Formation")
                            .setFont(fontBold)
                            .setFontSize(10)
                            .setFontColor(primaryColor)
                            .setTextAlignment(TextAlignment.LEFT))
                    .setBorder(null));
            footerTable.addCell(new Cell()
                    .add(new Paragraph("Email: contact@codingfactory.com\nSite: www.codingfactory.com")
                            .setFont(font)
                            .setFontSize(9)
                            .setFontColor(textColor)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(null));
            footerTable.addCell(new Cell()
                    .add(new Paragraph("© 2025 Coding Factory\nPage " + pdfDoc.getPageNumber(page))
                            .setFont(font)
                            .setFontSize(9)
                            .setFontColor(textColor)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(null));

            // Ajouter le tableau au canvas
            footerTable.setFixedPosition(40, 20, pageSize.getWidth() - 80);
            canvas.add(footerTable);
            canvas.close();
        });

        document.close();

        return baos.toByteArray();
    }

    // Utility method to escape HTML characters
    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}