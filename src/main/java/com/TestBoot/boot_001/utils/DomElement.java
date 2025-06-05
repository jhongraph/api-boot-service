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
    private String registrationVinImput;
    private String registrationSalePrice;
    private String saleDateInput;
    private String reimbursementDateInput;
    private String dealerLicenceInput;
    private String registrationDealerLicenceInput;
    private String registrationPlateInput;
    private String registrationTagInput;
    private String registrationContractInput;
    private String registrationContributoryPriceInput;
    private String registrationArbitragePriceInput;
    private String registrationLicenceInput;
    private String registrationConcessionaryName;
    private String registrationLicenceDateInput;
    private String registrationDoorInput;
    private String registrationYearInput;
    private String registrationMakeInput;
    private String registrationModelInput;
    private String registrationCilinInput;
    private String registrationPowerInput;
    private String concessionaryName;
    private String dealerLicenseDate;
    private String fileInputCssSelector;
    private String fileNameCellXpath;
    private String subTypeSelect;
    private String subTypeRegisterValue;
    private String registrationOrigenTittleValue;
    private String registrationCarUseTypeValue;
    private String registrationBuyTypeValue;
    private String registrationCarPropurtionTypeValue;
    private String colorSelect;
    private String registrationOrigenSaleDateSelect;
    private String registrationBuyType;
    private String registrationTagDateSelect;
    private String registrationPayDateSelect;
    private String registrationSelleDateSelect;
    private String registrationOrigenType;
    private String registrationColorSelect;
    private String registrationOdometer;
    private String registrationCarTypeSelect;
    private String registrationCarUseTypeSelect;
    private String registrationCarPropurtionTypeSelect;
    private String SubmitLoginButton;
    private String termConditionButton1;
    private String termConditionButton2;
    private String termConditionButton3;
    private String termConditionButton4;
    private String termConditionConfirmButton;
    private String preSaleButton;
    private String registerButton;
    private String vinSerchButton;
    private String dealerLicenseSerchButton;
    private String dateUserConfirmation;
    private String digitalCheckButton1;
    private String digitalCheckButton2;
    private String registrationDigitalCheckButton1;
    private String registrationDigitalCheckButton2;
    private String digitalCheckConfirmButton;
    private String registrationDataUserConfirmButton;
    private String registrationYes;
    private String registrationSerchLicenceButton;
    private String fileButtonCssSelector;
    private String continueButton;
    private String tryAgainButton;


}
