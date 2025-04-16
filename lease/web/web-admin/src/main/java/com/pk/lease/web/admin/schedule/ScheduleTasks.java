package com.pk.lease.web.admin.schedule;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pk.lease.model.entity.LeaseAgreement;
import com.pk.lease.model.enums.LeaseStatus;
import com.pk.lease.web.admin.service.LeaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//定时任务
@Component//将该类给spring容器管理
public class ScheduleTasks {
    //cron表达式
//    @Scheduled(cron = "* * * * * *")
//    public void test() {
//        //System.out.println(new Date());
//
//    }
    @Autowired
    private LeaseAgreementService leaseAgreementService;
    @Scheduled(cron = "0 0 0 * * *")
    public void checkLeaseStatus(){
        LambdaUpdateWrapper<LeaseAgreement> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.le(LeaseAgreement::getLeaseEndDate,new Date());
        lambdaUpdateWrapper.in(LeaseAgreement::getStatus,LeaseStatus.SIGNED,LeaseStatus.WITHDRAWING);
        lambdaUpdateWrapper.set(LeaseAgreement::getStatus, LeaseStatus.EXPIRED);
        leaseAgreementService.update(lambdaUpdateWrapper);
    }
}
