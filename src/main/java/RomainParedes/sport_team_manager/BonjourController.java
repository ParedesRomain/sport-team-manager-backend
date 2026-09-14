package RomainParedes.sport_team_manager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BonjourController {

    @GetMapping("/bonjour")
    public String direBonjour() {
        return "Bonjour le monde !";
    }
}