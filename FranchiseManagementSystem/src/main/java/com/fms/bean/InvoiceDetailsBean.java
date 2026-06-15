package com.fms.bean;

import com.fms.entity.Invoices;
import com.fms.entity.SaleItems;
import com.fms.service.InvoiceServiceLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;

@Named
@ViewScoped
public class InvoiceDetailsBean implements Serializable {

    @EJB
    private InvoiceServiceLocal invoiceService;

    private Integer id;
    private Invoices invoice;
   

    @PostConstruct
    public void init() {

        try {

            String param =
                FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getRequestParameterMap()
                    .get("id");

            if (param != null && !param.isEmpty()) {

                id = Integer.parseInt(param);

                invoice =
                    invoiceService.getInvoiceById(id);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public BigDecimal getLineTotal(SaleItems item) {

    return item.getPrice()
               .multiply(
                   BigDecimal.valueOf(item.getQuantity())
               );
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Invoices getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoices invoice) {
        this.invoice = invoice;
    }
}