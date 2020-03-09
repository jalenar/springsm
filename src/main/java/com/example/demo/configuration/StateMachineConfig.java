package com.example.demo.configuration;



import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import com.example.demo.model.ProcessEvent;
import com.example.demo.model.ProcessState;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by jt on 2019-07-23.
 */
@Slf4j
@EnableStateMachineFactory
@Configuration
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

    @Override
    public void configure(StateMachineTransitionConfigurer<ProcessState, ProcessEvent> transitions) throws Exception {
        transitions.withExternal().source(ProcessState.NEW).target(ProcessState.NEW).event(ProcessEvent.PRE_AUTHORIZE)
                .and()
                .withExternal().source(ProcessState.NEW).target(ProcessState.PRE_AUTH).event(ProcessEvent.PRE_AUTH_APPROVED)
                .and()
                .withExternal().source(ProcessState.NEW).target(ProcessState.PRE_AUTH_ERROR).event(ProcessEvent.PRE_AUTH_DECLINED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<ProcessState, ProcessEvent> config) throws Exception {
        StateMachineListenerAdapter<ProcessState, ProcessEvent> adapter = new StateMachineListenerAdapter<>(){
            @Override
            public void stateChanged(State<ProcessState, ProcessEvent> from, State<ProcessState, ProcessEvent> to) {
                log.info(String.format("stateChanged(from: %s, to: %s)", from, to));
            }
        };

        config.withConfiguration()
                .listener(adapter);
    }
}
