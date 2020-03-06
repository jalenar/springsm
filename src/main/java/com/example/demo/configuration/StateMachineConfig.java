package com.example.demo.configuration;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;

import com.example.demo.model.ProcessEvent;
import com.example.demo.model.ProcessState;
//@slf4j
@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<ProcessState, ProcessEvent> {
	 @Override
	    public void configure(StateMachineStateConfigurer<ProcessState, ProcessEvent> states) throws Exception {
	        states.withStates()
	                .initial(ProcessState.NEW)
	                .states(EnumSet.allOf(ProcessState.class))
	                .end(ProcessState.AUTH)
	                .end(ProcessState.PRE_AUTH_ERROR)
	                .end(ProcessState.AUTH_ERROR);
	    }
}
