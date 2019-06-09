package utility;

public class Pair<U, V> {

    public U v1;
    public V v2;

    public Pair(U u, V v){
        this.v1 = u;
        this.v2 = v;
    }

    public U getV1(){
        return this.v1;
    }

    public V getV2(){
        return this.v2;
    }
}
