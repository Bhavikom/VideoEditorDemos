package hu.agocs.rxmp4parser.filters;

import rx.functions.Func1;

public class NullFilter implements Func1<Object, Boolean> {

    @Override
    public Boolean call(Object o) {
        return o != null;
    }

}
