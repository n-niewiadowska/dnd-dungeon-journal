package com.dnd.backend;

import java.time.LocalDate;

import com.dnd.backend.constant.Ability;
import com.dnd.backend.dto.*;
import com.dnd.backend.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.dnd.backend.constant.CharacterClass;
import com.dnd.backend.constant.CharacterRace;
import com.dnd.backend.constant.GameStatus;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	 @Bean
	 public CommandLineRunner setup(
			 MainSkillService skillService,
			 MainItemService itemService,
			 DndCharacterService dndCharacterService,
			 CampaignService campaignService,
			 SessionService sessionService
	 ) {
	 	return args -> {
			 // items and skills
			MainItemDTO staff = itemService.createItem(MainItemDTO.builder()
					.name("Witch's Staff").description("Magical weapon only The Witch of the Wilds can use")
					.bonusValue(3).build());
			MainItemDTO shield = itemService.createItem(MainItemDTO.builder()
					.name("Templar's shield").description("Shield that counters all magical attacks")
					.bonusValue(5).build());

			MainSkillDTO skill1 = skillService.createSkill(MainSkillDTO.builder()
					.name("Freezing swords").description("Spell that makes all swords freeze enemies")
					.relatedAbility(Ability.STRENGTH).level(4).effect("+3 stamina")
					.associatedItem(staff).build());
			MainSkillDTO skill2 = skillService.createSkill(MainSkillDTO.builder()
					.name("Blocking").description("Higher possibility of blocking attacks")
					.relatedAbility(Ability.STRENGTH).level(6).effect("+4 defense")
					.associatedItem(shield).build());

			// characters
			DndCharacterDTO witch = dndCharacterService.createCharacter(DndCharacterDTO.builder()
					.firstName("Morrigan").lastName("Korcari").age(20).race(CharacterRace.HUMAN)
					.dndClass(CharacterClass.WIZARD).canPerformMagic(true).skill(skill1).build());
			DndCharacterDTO warrior = dndCharacterService.createCharacter(DndCharacterDTO.builder()
					.firstName("Alistair").lastName("Warden").age(27).race(CharacterRace.HUMAN)
					.dndClass(CharacterClass.FIGHTER).canPerformMagic(false).skill(skill2).build());

			// campaigns
			CampaignDTO campaign1 = campaignService.createCampaign(CampaignDTO.builder()
					.title("Call of Cthulhu").description("A mysterious cult worships Cthulhu, a monstrous human, octopus, dragon hybrid, who's dreams influence reality.")
					.beginningDate(LocalDate.of(2025, 1, 1)).status(GameStatus.IN_PROGRESS).build());
			CampaignDTO campaign2 = campaignService.createCampaign(CampaignDTO.builder()
					.title("Dungeons and Dragons").description("Exploring dungeons and slaying dragons")
					.beginningDate(LocalDate.of(2025, 2, 20)).status(GameStatus.PLANNED).build());
			CampaignDTO campaign3 = campaignService.createCampaign(CampaignDTO.builder()
					.title("Dragon Age Origins").description("Grey Warden, defeat the Archdemon and save the world from the Blight!")
					.beginningDate(LocalDate.of(2024, 6, 17)).status(GameStatus.FINISHED).build());

			// sessions
			SessionDTO session1 = sessionService.createSession(SessionDTO.builder()
					.campaign(campaign1).sessionDate(LocalDate.of(2025, 1, 1))
					.notes("The party reached a village and caught the trail of the cult.").build());
			SessionDTO session2 = sessionService.createSession(SessionDTO.builder()
					.campaign(campaign3).sessionDate(LocalDate.of(2024, 7, 3))
					.notes("Redcliffe was saved from the Darkspawn, the company went on to save Arl in his castle").build());
			SessionDTO session3 = sessionService.createSession(SessionDTO.builder()
					.campaign(campaign3).sessionDate(LocalDate.of(2024, 9, 30))
					.notes("The Circle of Magi is in danger! Will the party save it?").build());

			// add characters and sessions to their campaigns
			campaignService.addCharacterToCampaign(campaign1.id(), witch.id());
			campaignService.addCharacterToCampaign(campaign3.id(), witch.id());
			campaignService.addCharacterToCampaign(campaign3.id(), warrior.id());

			campaignService.addSessionToCampaign(campaign1.id(), session1.id());
			campaignService.addSessionToCampaign(campaign3.id(), session2.id());
			campaignService.addSessionToCampaign(campaign3.id(), session3.id());

			System.out.println("Data initialized successfully!");
		};
	 }
}
