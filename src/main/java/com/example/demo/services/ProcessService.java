package com.example.demo.services;



import org.springframework.statemachine.StateMachine;

import com.example.demo.model.ProcessEvent;
import com.example.demo.model.ProcessState;
import com.example.demo.model.Processpay;

/**
 * Created by jt on 2019-08-10.
 */
public interface ProcessService {

    Processpay newPayment(Processpay payment);

    StateMachine<ProcessState, ProcessEvent> preAuth(Long paymentId);

    StateMachine<ProcessState, ProcessEvent> authorizePayment(Long paymentId);

    StateMachine<ProcessState, ProcessEvent> declineAuth(Long paymentId);
}