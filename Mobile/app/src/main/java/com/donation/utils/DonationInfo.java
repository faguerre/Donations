package com.donation.utils;

import com.donation.model.AddressModel;

import java.util.List;

public class DonationInfo {
    private static List<AddressModel> points;

    public static List<AddressModel> getPoints() {
        return points;
    }

    public static void setPoints(List<AddressModel> points) {
        DonationInfo.points = points;
    }
}
