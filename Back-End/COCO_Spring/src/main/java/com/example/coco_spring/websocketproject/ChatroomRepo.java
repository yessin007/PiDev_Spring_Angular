package com.example.coco_spring.websocketproject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepo extends JpaRepository<Chatroom, Long>{

}
