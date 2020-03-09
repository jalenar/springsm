package com.example.demo.configuration;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;

import com.example.demo.model.ProcessEvent;
import com.example.demo.model.ProcessState;
@SpringBootTest

public class StateMachineConfigTest extends StateMachineConfigurerAdapter<ProcessState, ProcessEvent> {

    
	@Autowired
    StateMachineFactory<ProcessState, ProcessEvent> factory;
    @Test
    void sample() {
    StateMachine<ProcessState, ProcessEvent> sm = factory.getStateMachine(UUID.randomUUID());

    sm.start();
    	
   // System.out.println(sm.getState().toString());
    }
}
