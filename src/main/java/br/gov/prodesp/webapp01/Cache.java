/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.prodesp.webapp01;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementa um cache simples em memória através de uma classe de cache que
 * segue o padrão Singleton. OBS: Este cache não é thread-safe e deve ser
 * utilizado apenas para testes.
 *
 * @author Marcelo
 */
public class Cache {

    private static Cache instance;
    private Map<String, Element> map;
    private int TTL_PADRAO = 1440; // Tempo padrão: um dia.

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    private Cache() {
        map = new HashMap<>();
    }

    /**
     * Insere um elemento no cache, com tempo de vida padrão.
     *
     * @param key Chave identificadora
     * @param object Elemento a ser inserido
     */
    public void put(String key, Object object) {
        this.put(key, object, TTL_PADRAO);
    }

    /**
     * Insere um elemento no cache, com tempo de vida determinado.
     *
     * @param key Chave identificadora
     * @param object Elemento a ser inserido
     * @param ttl Tempo de vida (em minutos)
     */
    public void put(String key, Object object, int ttl) {
        Element element = new Element(object, ttl);
        if (ttl > 0) {
            map.put(key, element);
        }
    }

    public Object get(String key) {
        Element element = map.get(key);
        if (element == null) {
            return null;
        }
        if (element.isExpired()) {
            map.remove(key);
            return null;
        }
        return element.getValue();
    }

    private void clearExpiredElements() {
        for (Map.Entry<String, Element> entry : map.entrySet()) {
            String key = entry.getKey();
            Element value = entry.getValue();
            if (value.isExpired()) {
                map.remove(key);
            }
        }
    }

    private class Element {

        private Object value;
        private Calendar expireTime;

        public Element(Object value, int ttl) {
            this.value = value;
            expireTime = Calendar.getInstance();
            expireTime.add(Calendar.MINUTE, ttl);
        }

        public Object getValue() {
            return value;
        }

        public boolean isExpired() {
            Calendar now = Calendar.getInstance();
            return now.compareTo(expireTime) > 0;
        }
    }

}
