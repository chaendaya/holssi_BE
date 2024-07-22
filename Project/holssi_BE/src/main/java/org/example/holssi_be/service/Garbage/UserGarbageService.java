package org.example.holssi_be.service.Garbage;

import org.example.holssi_be.dto.Garbage.RegisterGarbageDTO;
import org.example.holssi_be.entity.domain.Garbage;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.repository.GarbageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserGarbageService {

    @Autowired
    private GarbageRepository garbageRepository;

    // 쓰레기 등록 - User
    public void registerGarbage(RegisterGarbageDTO registerGarbageDTO, Member member) {
        Garbage garbage = create(registerGarbageDTO, member);
        garbageRepository.save(garbage);
    }

    // 쓰레기 등록 DTO -> Entity
    private Garbage create(RegisterGarbageDTO dto, Member member) {
        double organicWeight = dto.getOrganicWeight();
        double non_organicWeight = dto.getNon_organicWeight();
        double totalValue = 60 * organicWeight + 80 * non_organicWeight;

        Garbage garbage = new Garbage();
        garbage.setUser(member.getUser());
        garbage.setOrganicWeight(organicWeight);
        garbage.setNon_organicWeight(non_organicWeight);
        garbage.setTotalWeight(organicWeight + non_organicWeight);
        garbage.setTotalValue(totalValue);
        garbage.setMatched(false);
        garbage.setLocation(member.getUser().getLocation());
        garbage.setRegistrationDate(new Date());

        return garbage;
    }

    /*public List<RegisteredGarbageDTO> getRegisteredGarbages(Long user_id) {
        return garbageRepository.findByMatchedAndUserId(true, user_id)
                .stream()
                .map(this::convertToRegisteredGarbageDTO)
                .collect(Collectors.toList());
    }

    private RegisteredGarbageDTO convertToRegisteredGarbageDTO(Garbage garbage) {
        RegisteredGarbageDTO dto = new RegisteredGarbageDTO();
        dto.setGarbageId(garbage.getId());
        dto.setMatched(garbage.isMatched());
        dto.setStartCollection(garbage.isStartCollection());
        dto.setCollectorName(garbage.get);



        return dto;
    }*/
}
