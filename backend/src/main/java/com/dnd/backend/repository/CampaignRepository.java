package com.dnd.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dnd.backend.domain.Campaign;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {} 
