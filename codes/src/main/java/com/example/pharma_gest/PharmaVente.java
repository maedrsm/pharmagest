package com.example.pharma_gest;

public class PharmaVente {

    private int id_medic, quantity;
    private float price;
    private String name, forme;

    public static class medicaments {

        int id, qty;
        float price;
        String name, forme;
        public medicaments(int id, int qty, String name, float price) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.forme = forme;
            this.qty = qty;}

        public int getId() {
            return id;
        }
        public String getName() { return name;}

        public int getQty() {
            return qty;
        }

        public String getForme() {return forme;}

        public float getPrice() { // Corrected method name to getPrix
            return price;
        }}}
