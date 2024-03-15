package services;

import dataAccess.UserRepository;
import dtos.TransactionQueueDto;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import models.UserEntity;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@WebListener("Transaction queue listener!")
public class TransactionQueueService implements ServletContextListener {
    private static volatile Queue<TransactionQueueDto> instance;
    private static final Object mutex = new Object();
    private UserRepository userRepository;

    public static Queue<TransactionQueueDto> getInstance() {
        Queue<TransactionQueueDto> result = instance;
        if (instance == null) {
            synchronized (mutex){
                result = instance;
                if(result == null){
                    instance = result = new ConcurrentLinkedQueue<>();
                }
            }
        }

        return result;
    }

    public TransactionQueueService() {
        userRepository = new UserRepository();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Transaction queue initialize");
        ServletContext context = sce.getServletContext();
        context.setAttribute("transaction_queue", getInstance());

        Thread consumerThread = new Thread(() -> {
            while (true) {
                //TODO: change to get the head element, if logic is success remove it
                TransactionQueueDto element = instance.poll();
                if (element != null) {
                    System.out.println("Processing element: " + element.getAmount());
                    System.out.println("Processing element: " + element.getUserId());
                    System.out.println("Processing element: " + element.getAction());

                    Optional<UserEntity> optionalUserEntity = userRepository
                            .getUserById(element.getUserId());

                    if(optionalUserEntity.isEmpty()){
                        System.out.println("User not found!");
                        continue;
                    }
                    BigInteger newBalance = optionalUserEntity.get().getBalance()
                            .add(element.getAmount());
                    userRepository.updateUserBalance(element.getUserId(), newBalance);
                } else {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        consumerThread.start();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Transaction queue destroyed");
        ServletContext context = sce.getServletContext();
        context.setAttribute("transaction_queue", null);
    }
}
