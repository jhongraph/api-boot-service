package com.TestBoot.boot_001.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "elements")
public class DomElement {
    private String usernameInput;
    private String passwordInput;
    private String vinInput;
    private String plateInput;
    private String yearInput;
    private String makeInput;
    private String modelInput;
    private String contractNumberInput;
    private String licenceInput;
    private String saleDateInput;
    private String reimbursementDateInput;
    private String dealerLicenceInput;
    private String concessionaryName;
    private String dealerLicenseDate;
    private String fileInputCssSelector;
    private String fileNameCellXpath;
    private String subTypeSelect;
    private String subTypeRegisterValue;
    private String colorSelect;
    private String SubmitLoginButton;
    private String termConditionButton1;
    private String termConditionButton2;
    private String termConditionButton3;
    private String termConditionButton4;
    private String termConditionConfirmButton;
    private String preSaleButton;
    private String vinSerchButton;
    private String dealerLicenseSerchButton;
    private String dateUserConfirmation;
    private String digitalCheckButton1;
    private String digitalCheckButton2;
    private String digitalCheckConfirmButton;
    private String fileButtonCssSelector;
    private String continueButton;
    private String tryAgainButton;



}
