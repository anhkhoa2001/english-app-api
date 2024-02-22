package org.base.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.model.CountRequestModel;
import org.base.repositories.CountRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.Date;

@Component
@Slf4j
public class CountRequestManager extends ThreadManager<CountRequestModel>{

    @Autowired
    private CountRequestRepo countRequestRepo;

    @Override
    public void doProcess(ArrayList<CountRequestModel> items) {
        log.info("do logging count request");
        taskExecutor.submit(() -> {
            try {
                log.info("{}", new ObjectMapper().writeValueAsString(items));
                log.info("{}", items.size());

                countRequestRepo.saveAll(items);
            } catch (Exception e) {
                log.error(e.toString(), e);
                return 0;
            }
            log.info("END LogImpactThread call");
            return 1;
        });
    }

    @PostConstruct
    private void init() {
        log.info("Init queue receive master data.....");
        listen();
    }

    @PreDestroy
    private void destroy() {
        log.info("Destroy queue receive master data.....");
        stop();
    }

    @Override
    public String getName() {
        return "hihi";
    }
}
