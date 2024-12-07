package org.featherlessbipeds.watchdog.service;

import lombok.RequiredArgsConstructor;
import org.featherlessbipeds.watchdog.entity.Condominium;
import org.featherlessbipeds.watchdog.repository.CondominiumRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CondominiumService {

    private final CondominiumRepository repository;

    public void registerCondominium(Condominium condominium){
        repository.save(condominium);

    }

}
