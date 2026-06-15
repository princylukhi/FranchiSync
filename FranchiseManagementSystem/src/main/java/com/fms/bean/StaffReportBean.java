package com.fms.bean;

import com.fms.entity.Users;

import com.fms.service.SalesServiceLocal;
import com.fms.service.InvoiceServiceLocal;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpSession;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class StaffReportBean
implements Serializable {

    @EJB
    private SalesServiceLocal salesService;

    @EJB
    private InvoiceServiceLocal invoiceService;

    private int staffId;

    // SALES REVENUE

    private String salesLabels;
    private String salesData;

    // PAYMENT MODE

    private String paymentLabels;
    private String paymentData;

    // INVOICE TREND

    private String invoiceLabels;
    private String invoiceData;

    @PostConstruct
    public void init() {

        HttpSession session =
            (HttpSession) FacesContext
            .getCurrentInstance()
            .getExternalContext()
            .getSession(false);

        if(session == null) {
            return;
        }

        Users user =
            (Users) session.getAttribute("user");

        if(user == null) {
            return;
        }

        staffId =
            user.getUid();

        loadSalesRevenue();

        loadPaymentDistribution();

        loadInvoiceTrend();
    }

    // ==========================
    // MONTHLY SALES REVENUE
    // ==========================

    private void loadSalesRevenue() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            salesService
            .getMonthlySalesRevenueByStaff(
                staffId
            );

        String[] months = {

            "",
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        };

        for(Object[] row : result) {

            labels.append("'")
                  .append(
                      months[
                          Integer.parseInt(
                              row[0].toString()
                          )
                      ]
                  )
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        salesLabels =
            labels.toString();

        salesData =
            data.toString();
    }

    // ==========================
    // PAYMENT MODE DISTRIBUTION
    // ==========================

    private void loadPaymentDistribution() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            salesService
            .getPaymentModeDistributionByStaff(
                staffId
            );

        for(Object[] row : result) {

            labels.append("'")
                  .append(row[0])
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        paymentLabels =
            labels.toString();

        paymentData =
            data.toString();
    }

    // ==========================
    // MONTHLY INVOICE TREND
    // ==========================

    private void loadInvoiceTrend() {

        StringBuilder labels =
            new StringBuilder();

        StringBuilder data =
            new StringBuilder();

        List<Object[]> result =
            invoiceService
            .getMonthlyInvoiceTrendByStaff(
                staffId
            );

        String[] months = {

            "",
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        };

        for(Object[] row : result) {

            labels.append("'")
                  .append(
                      months[
                          Integer.parseInt(
                              row[0].toString()
                          )
                      ]
                  )
                  .append("',");

            data.append(row[1])
                .append(",");
        }

        invoiceLabels =
            labels.toString();

        invoiceData =
            data.toString();
    }

    // ==========================
    // GETTERS
    // ==========================

    public String getSalesLabels() {
        return salesLabels;
    }

    public String getSalesData() {
        return salesData;
    }

    public String getPaymentLabels() {
        return paymentLabels;
    }

    public String getPaymentData() {
        return paymentData;
    }

    public String getInvoiceLabels() {
        return invoiceLabels;
    }

    public String getInvoiceData() {
        return invoiceData;
    }
}