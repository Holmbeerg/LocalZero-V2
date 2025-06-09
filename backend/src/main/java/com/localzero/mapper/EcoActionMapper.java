package com.localzero.mapper;

import com.localzero.model.EcoAction;
import com.localzero.model.dto.EcoActionResponse;
import org.springframework.stereotype.Component;

@Component
public class EcoActionMapper {

    public EcoActionResponse toResponse(EcoAction ecoAction) {
        return new EcoActionResponse(
                ecoAction.getId(),
                ecoAction.getActionDate().toString(),
                ecoAction.getEcoActionType().getAction(),
                ecoAction.getEcoActionType().getCategory(),
                ecoAction.getEcoActionType().getCarbonSaved()
        );
    }
}
