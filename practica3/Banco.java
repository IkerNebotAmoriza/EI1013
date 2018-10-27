package practica3;

import java.util.*;

public class Banco {
    public HashMap<String, Integer> cuentas = new HashMap<>();                // Mapa con las cuentas y su saldos
    public HashMap<String, List<Transferencia>> desglose = new HashMap<>();  // Mapa con las trasnferencias por cada cuenta

    public Banco(List<String> codigos, List<Integer> saldos) throws IllegalArgumentException{
        if (codigos == null || saldos == null || codigos.size() != saldos.size() || codigos.contains(null) || saldos.contains(null)){
            throw new IllegalArgumentException();
        }

       for (int i = 0; i < codigos.size(); i++) {
           cuentas.put(codigos.get(i),saldos.get(i));
       }
    }

    /**
     * Realiza una una trasnferencia entres dos cuentas modificando su saldo. Guarda la transferencia en un histórico.
     *
     *
     * @param tr La transferencia. Los códigos de cuenta deben existir y ser distintos de <i>null</i>. La cuenta origen debe
     *           tener saldo positivo suficiente para realizar la transferencia.
     * @return <i>True</i> si la transferencia fue posible. ç
     * @throws <i>IllegalArgumentExcpetion</i> si alguna de las cuentas no existe, o el código es <i>null</i>.
     */
    public boolean asiento(Transferencia tr) throws IllegalArgumentException{
        if (tr == null || !cuentas.containsKey(tr.origen) || !cuentas.containsKey(tr.destino)) {
            throw new IllegalArgumentException();
        }
        Transferencia trAux = new Transferencia(tr);

        if (trAux.cantidad < 0){
            trAux.invertir();
        }
        if (cuentas.get(trAux.origen) < trAux.cantidad) {
            return false;
        }

        cuentas.put(trAux.origen, cuentas.get(trAux.origen)-trAux.cantidad);
        cuentas.put(trAux.destino, cuentas.get(trAux.destino)+trAux.cantidad);

        if (desglose.containsKey(trAux.origen)) {
            desglose.get(trAux.origen).add(trAux);
        }else {
            desglose.put(trAux.origen, new ArrayList<Transferencia>());
            desglose.get(trAux.origen).add(trAux);
        }
        if (desglose.containsKey(trAux.destino)) {
            desglose.get(trAux.destino).add(trAux);
        }else {
            desglose.put(trAux.destino, new ArrayList<Transferencia>());
            desglose.get(trAux.destino).add(trAux);
        }

        return true;
    }

    /**
     * Devuelve el saldo de una cuenta
     * @param codigo Código de la cuenta. Debe ser distinto <i>null</i> y existir.
     * @return El saldo de la cuenta.
     * @throws <i>IllegalArgumentException</i> si el código de cuenta no es válido.
     */
    public Integer consulta(String codigo) throws IllegalArgumentException{
        if(codigo == null || !cuentas.containsKey(codigo)){
            throw new IllegalArgumentException();
        }

        return cuentas.get(codigo);
    }

    /**
     *  Devuelve el histórico de transferencias entre dos cuentas.
     * @param primera Código válido de cuenta
     * @param segunda Código válido de cuenta
     * @return Lista de transferencias. El código <i> primera</i> siempre aparecerá como cuenta de origen de cada
     *          transferencia. En caso de el código de cuenta sea el mismo la lista estará vacía.
     * @throws <i>IllegalArgumentExcpetion</i> si alguno de los códigos de cuenta no son válidos.
     */
    public List<Transferencia> historico(String primera, String segunda) {
        if(primera == null || !desglose.containsKey(primera) || segunda == null || !desglose.containsKey(segunda)){
            throw new IllegalArgumentException();
        }
        if (primera.equals(segunda)) {
            return new ArrayList<>();
        }
        List<Transferencia> sol = new ArrayList<Transferencia>();
        ListIterator<Transferencia> iter = new ArrayList<Transferencia>(desglose.get(primera)).listIterator();
        Transferencia aux;
        int cantidad;

        while (iter.hasNext()){
            aux = iter.next();
            cantidad = aux.cantidad;

            if (!aux.origen.equals(primera)) {
                cantidad = -cantidad;
            }

            if (aux.origen.equals(segunda) || aux.destino.equals(segunda)) {
                sol.add(new Transferencia(primera,segunda,cantidad));
            }
        }
        return sol;
    }

    public String toString() {
        StringBuilder bf = new StringBuilder("Banco - cuentas {\n") ;

        for (String cod: cuentas.keySet())
            bf.append("  " + cod + ": " +cuentas.get(cod)+ "\n");


        bf.append("} - size:" + cuentas.size());

        return bf.toString();
    }

    public String toStringDesglose() {
        StringBuilder bf = new StringBuilder("Banco - desglose {\n");

        for (String cod: desglose.keySet())
            bf.append("  " + cod + ": " + desglose.get(cod));


        bf.append("}");
        return bf.toString();
    }

}
