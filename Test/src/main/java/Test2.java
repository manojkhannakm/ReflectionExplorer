/**
 * @author Manoj Khanna
 */

public class Test2 {

    public static final int I_1 = 1;
    protected static final int I_2 = 2;
    static final int I_3 = 3;
    private static final int I_4 = 4;

    public static int i5 = 5;
    protected static int i6 = 6;
    static int i7 = 7;
    private static int i8 = 8;

    public final int i9 = 9;
    protected final int i10 = 10;
    final int i11 = 11;
    private final int i12 = 12;

    public int i13 = 13;
    protected int i14 = 14;
    int i15 = 15;
    private int i16 = 16;

    public static void main(String[] args) {
        new Test21.Test22.Test23();
    }

    public static class Test21 {

        public int i = 0;

        public static class Test22 {

            public int i = 0;

            public static class Test23 {

                public int i = 0;

            }

        }

    }

}
