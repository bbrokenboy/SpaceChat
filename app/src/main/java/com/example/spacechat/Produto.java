package com.example.spacechat;

import java.util.UUID;

public class Produto {

    private String nomeProduto;
    private String imgProduto;
    private String precoProduto;
    private String descricaoProduto;
    private String idProduto;

    public Produto(String idProduto, String nomeProduto, String imgProduto, String precoProduto, String descricaoProduto) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.imgProduto = imgProduto;
        this.precoProduto = precoProduto;
        this.descricaoProduto = descricaoProduto;
    }

    public Produto(){

    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String getImgProduto() {
        return imgProduto;
    }

    public void setImgProduto(String imgProduto) {
        this.imgProduto = imgProduto;
    }

    public String getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(String precoProduto) {
        this.precoProduto = precoProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }
}
