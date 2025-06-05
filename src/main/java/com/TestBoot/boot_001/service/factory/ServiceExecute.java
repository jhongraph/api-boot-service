package com.TestBoot.boot_001.service.factory;

import com.TestBoot.boot_001.pojos.PreSaleRequest;

public interface ServiceExecute {

    void execute(PreSaleRequest request) throws InterruptedException;
}
