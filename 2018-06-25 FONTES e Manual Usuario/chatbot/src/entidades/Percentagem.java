/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author Lucas Sens Faht
 */
public class Percentagem {
    private Frase frase;
    private float percentagem;

    public Percentagem(Frase f, float p) {
        this.frase = f;
        this.percentagem = p;
    }

    public Frase getFrase() {
        return frase;
    }

    public float getPercentagem() {
        return percentagem;
    }    
}
