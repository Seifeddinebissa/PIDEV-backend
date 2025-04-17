package tn.esprit.gestionformation.ClientFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "user-s", url = "http://localhost:8080")
public interface userClient {

    @GetMapping("/get-by-id/{id}")
    public userClient findById(@PathVariable("id") Long id);

}
