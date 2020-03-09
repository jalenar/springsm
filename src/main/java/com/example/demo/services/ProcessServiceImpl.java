package com.example.demo.services;



import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import com.example.demo.model.ProcessEvent;
import com.example.demo.model.ProcessState;
import com.example.demo.model.Processpay;

import lombok.RequiredArgsConstructor;

/**
 * Created by jt on 2019-08-10.
 */
@RequiredArgsConstructor
@Service
public class ProcessServiceImpl implements ProcessService {
    public static final String PAYMENT_ID_HEADER = "payment_id";

    private  StateMachineFactory<ProcessState, ProcessEvent> stateMachineFactory;

	private ProcessPayRepository paymentRepository;

    @Override
    public Processpay newPayment(Processpay payment) {
        payment.setState(ProcessState.NEW);
        return paymentRepository.save(payment);
    }

    @Override
    public StateMachine<ProcessState, ProcessEvent> preAuth(Long paymentId) {
        StateMachine<ProcessState, ProcessEvent> sm = build(paymentId);

        sendEvent(paymentId, sm, ProcessEvent.PRE_AUTHORIZE);

        return null;
    }

    @Override
    public StateMachine<ProcessState, ProcessEvent> authorizePayment(Long paymentId) {
        StateMachine<ProcessState, ProcessEvent> sm = build(paymentId);

        sendEvent(paymentId, sm, ProcessEvent.AUTH_APPROVED);

        return null;
    }

    @Override
    public StateMachine<ProcessState, ProcessEvent> declineAuth(Long paymentId) {
        StateMachine<ProcessState, ProcessEvent> sm = build(paymentId);

        sendEvent(paymentId, sm, ProcessEvent.AUTH_DECLINED);

        return null;
    }

    private void sendEvent(Long paymentId, StateMachine<ProcessState, ProcessEvent> sm, ProcessEvent event){
        Message msg = MessageBuilder.withPayload(event)
                .setHeader(PAYMENT_ID_HEADER, paymentId)
                .build();

        sm.sendEvent(msg);
    }

    private StateMachine<ProcessState, ProcessEvent> build(Long paymentId){
    	Processpay payment = paymentRepository.getOne(paymentId);

        StateMachine<ProcessState, ProcessEvent> sm = stateMachineFactory.getStateMachine(Long.toString(payment.getId()));

        sm.stop();

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.resetStateMachine(new DefaultStateMachineContext<>(payment.getState(), null, null, null));
                });

        sm.start();

        return sm;
    }
}
