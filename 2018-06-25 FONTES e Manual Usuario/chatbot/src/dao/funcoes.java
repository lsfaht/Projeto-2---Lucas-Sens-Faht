/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import entidades.Frase;
import entidades.Percentagem;
import entidades.Respostas;
import entidades.Significado;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import javax.swing.*;

/**
 *
 * @author Lucas Sens Faht
 */
public class funcoes {

    conexao con = new conexao();
    FraseJpaController jpaFrase = new FraseJpaController(con.getConexao());
    RespostasJpaController jpaResposta = new RespostasJpaController(con.getConexao());
    SignificadoJpaController jpaSignificado = new SignificadoJpaController(con.getConexao());

    public void inserirSignificado(String pal) {
        jpaSignificado.create(new Significado(null, pal));
    }

    public void preencherSignificado(JComboBox cb) {
        List<Significado> l = jpaSignificado.findSignificadoEntities();

        for (int i = 0; i < l.size(); i++) {
            cb.addItem(l.get(i).getSignificado());
        }
    }

    public Significado getSignificado(String sig) {
        List<Significado> lstSig = jpaSignificado.findSignificadoEntities();

        Significado s = new Significado();

        for (int i = 0; i < lstSig.size(); i++) {
            if (lstSig.get(i).getSignificado().equals(sig)) {
                s = lstSig.get(i);
            }
        }
        return s;
    }

    public void inserirFrase(String frase, String significado) {
        //System.out.println("A frase e: "+frase+" \n O significado e: "+getSignificado(significado).getId());
        jpaFrase.create(dadosFrase(frase, significado));
    }

    public Frase dadosFrase(String f, String s) {
        Frase fa = new Frase();
        fa.setFrase(f);
        fa.setId(null);
        fa.setIdSignificado(getSignificado(s));

        return fa;
    }

    public void listarFrase(JComboBox c) {
        List<Frase> fa = jpaFrase.findFraseEntities();

        for (int i = 0; i < fa.size(); i++) {
            c.addItem(fa.get(i).getFrase());
        }
    }

    public int getIdFrase(String fa) {
        List<Frase> f = jpaFrase.findFraseEntities();
        int num = 0;

        for (int i = 0; i < f.size(); i++) {
            if (f.get(i).getFrase().equals(fa)) {
                num = f.get(i).getId();
            }
        }

        return num;
    }

    public void inserirResposta(String resposta) {
        jpaResposta.create(getResposta(resposta));
    }

    public Respostas getResposta(String r) {
        Respostas re = new Respostas();
        re.setId(null);
        re.setIdFrase(fraseId(r));
        return re;
    }

    public Frase fraseId(String f) {
        Frase fa = new Frase();

        fa.setId(getIdFrase(f));

        return fa;
    }

    public String tirarEspeciais(String aux) {
        String nova = new String();

        for (int i = 0; i < aux.length(); i++) {
            if ((aux.charAt(i) == ',') || (aux.charAt(i) == '.') || (aux.charAt(i) == ';') || (aux.charAt(i) == ':')
                    || (aux.charAt(i) == '?') || (aux.charAt(i) == '!') || (aux.charAt(i) == '+') || (aux.charAt(i) == '*')
                    || (aux.charAt(i) == '/') || (aux.charAt(i) == '#')) {

            } else {
                nova += aux.charAt(i);
            }
        }

        return nova;
    }

    public boolean eLetra(char c) {
        if ((c >= 'a' && c <= 'z') || (c >= 'A' || c <= 'Z')) {
            return true;
        }

        return false;
    }

    public boolean eNumero(char c) {
        if (c >= 0 && c <= 9) {
            return true;
        }

        return false;
    }

    public String[] fraseDividida(String frase) {
        ArrayList<String> a = new ArrayList();

        String[] b = frase.split(" ");
        
        for (String c : b) {
            a.add(c);
        }

        a = metodoLokoQueNemEuEntendo(a, "\t");
        a = metodoLokoQueNemEuEntendo(a, "\r");
        a = metodoLokoQueNemEuEntendo(a, "\n");

        String aux[] = new String[a.size()];
        
        for (int d = 0; d < a.size(); d++) {
            aux[d] = a.get(d);
        }
        
        return aux;
    }

    public ArrayList<String> metodoLokoQueNemEuEntendo(ArrayList<String> loko, String split) {
        ArrayList a = new ArrayList();

        for (String b : loko) {
            String[] c = b.split(split);
            for (String d : c) {
                a.add(d);
            }
        }

        return a;
    }

    public void resposta(JTextArea area, String[] entrada) {
        List<Frase> lstFrase = jpaFrase.findFraseEntities();
        ArrayList<Percentagem> per = new ArrayList<>();

        for (int i = 0; i < lstFrase.size(); i++) {
            String[] f = fraseDividida(tirarEspeciais(lstFrase.get(i).getFrase()));

            if (f.length > entrada.length) {
                per.add(new Percentagem(lstFrase.get(i), percentagem(f, entrada)));
            } else if (f.length < entrada.length) {
                per.add(new Percentagem(lstFrase.get(i), percentagem(entrada, f)));
            } else {
                per.add(new Percentagem(lstFrase.get(i), percentagem(f, entrada)));
            }
        }
        List<Respostas> resposatsLista = repo(getFrasePercentagem(per));

//        for(int p=0; p<resposatsLista.size(); p++){
//            System.out.println(""+resposatsLista.get(p).getIdFrase().getFrase());
//        }
        Random ram = new Random();
        area.append("Atendente: " + resposatsLista.get(ram.nextInt(resposatsLista.size())).getIdFrase().getFrase() + "\n\n");
        //   area.append("Atendente: "+getFrasePercentagem(per).getFrase()+"\n\n");
        limparLista(resposatsLista);
    }

    public boolean iguais(String a, String b) {
        if (a.equals(b)) {
            return true;
        }
        return false;
    }

    public float percentagem(String a[], String b[]) {
        int cont = 0;
        float percentagem = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if (iguais(a[i], b[j]) == true) {
                    cont++;
                }
            }
        }
        percentagem = (100 * cont) / a.length;

        return percentagem;
    }

    public void mostrarMapa(HashMap<Integer, Float> mapa) {
        Set set = mapa.entrySet();
        Iterator it = set.iterator();

        while (it.hasNext()) {
            Map.Entry ent = (Map.Entry) it.next();
            System.out.println("id Frase: " + ent.getKey() + "  percentagem: " + ent.getValue());
        }
    }

    public Frase getFrasePercentagem(ArrayList<Percentagem> p) {
        float maior = 0;

        for (int i = 0; i < p.size(); i++) {
            if (maior < p.get(i).getPercentagem()) {
                maior = p.get(i).getPercentagem();
            }
        }

        for (int j = 0; j < p.size(); j++) {
            if (maior == p.get(j).getPercentagem()) {
                return p.get(j).getFrase();
            }
        }
        return null;
    }

    public List<Respostas> repo(Frase f) {
        List<Respostas> repo = jpaResposta.findRespostasEntities();
        List<Respostas> ret = new ArrayList<Respostas>();

        for (int i = 0; i < repo.size(); i++) {
            if (Objects.equals(repo.get(i).getIdFrase().getId(), f.getId())) {
                ret.add(repo.get(i));
            }
        }
        return ret;
    }

    public void limparLista(List<Respostas> res) {
        res.clear();
    }
}
