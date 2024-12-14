package org.featherlessbipeds.watchdog.controller;

import lombok.RequiredArgsConstructor;
import org.featherlessbipeds.watchdog.DTO.CondominiumDTO;
import org.featherlessbipeds.watchdog.DTO.CondominiumRegisterDTO;
import org.featherlessbipeds.watchdog.DTO.CondominiumLoginDTO;
import org.featherlessbipeds.watchdog.entity.Condominium;
import org.featherlessbipeds.watchdog.service.CondominiumService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/condom")
@RequiredArgsConstructor
public class CondominiumController
{
    private final CondominiumService service;

    @PostMapping("/register")
    public ResponseEntity<String> registerCondominium(@RequestBody CondominiumRegisterDTO condom)
    {
        Condominium condominium = new Condominium();
        condominium.setLocation(condom.location());
        condominium.setName(condom.name());
        condominium.setEmail(condom.email());
        //A senha ta sem hash, dps v isso
        condominium.setPasswordHash(condom.passwordHash());

        try
        {
            service.registerCondominium(condominium);
            return ResponseEntity.status(HttpStatus.CREATED).body("Condominio criado");
        }
        catch (DataIntegrityViolationException e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade (Talvez a entidade ja exista): " + e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("BOoooM a bomba explodiu: " + e.getMessage());
        }
    }

//  curl -X POST "http://localhost:8080/condom/login" -H "Content-Type: application/json" -d "{\"email\":\"trustee1@sunrisevillas.com\",\"password\":\"123\"}"
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CondominiumLoginDTO loginDTO)
    {
        var condominium = service.loginCondominium(loginDTO.email(), loginDTO.password());

        if (condominium != null)
        {
            var condominiumDTO = new CondominiumDTO(condominium.getName(), condominium.getTrusteeName(), condominium.getLocation());
            return ResponseEntity.status(HttpStatus.OK).body(condominiumDTO);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email e/ou senha inválidos.");
    }

}
