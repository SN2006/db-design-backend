package com.example.app.dbdesignbackend.service;

import com.example.app.dbdesignbackend.dao.TopicDAO;
import com.example.app.dbdesignbackend.dto.CreateTopicDTO;
import com.example.app.dbdesignbackend.dto.TopicDTO;
import com.example.app.dbdesignbackend.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TopicService {

    private TopicDAO topicDAO;

    public List<TopicDTO> findAll() {
        return topicDAO.getAllTopics();
    }

    public List<TopicDTO> findAllNotRelatedToCourse(int courseId) {
        return topicDAO.getTopicsNotRelatedToCourse(courseId);
    }

    public TopicDTO findById(int id) {
        return topicDAO.getTopicById(id).orElseThrow(() ->
                new EntityNotFoundException("Topic with id " + id + " not found"));
    }

    public void createTopic(CreateTopicDTO createTopicDTO) {
        topicDAO.createTopic(createTopicDTO);
    }

    public void updateTopic(int id, CreateTopicDTO updateTopicDTO) {
        if (topicDAO.getTopicById(id).isEmpty()) {
            throw new EntityNotFoundException("Topic with id " + id + " not found");
        }
        topicDAO.updateTopic(id, updateTopicDTO);
    }

    public void deleteTopic(int id) {
        if (topicDAO.getTopicById(id).isEmpty()) {
            throw new EntityNotFoundException("Topic with id " + id + " not found");
        }
        topicDAO.deleteTopic(id);
    }

}
