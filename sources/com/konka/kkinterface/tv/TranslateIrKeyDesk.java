package com.konka.kkinterface.tv;

public class TranslateIrKeyDesk {
    static TranslateIrKeyDesk keyinstance = null;
    final int[][] mstar_ir_keymap;

    public TranslateIrKeyDesk() {
        int[] iArr = new int[2];
        iArr[0] = 255;
        this.mstar_ir_keymap = new int[][]{iArr};
    }

    public static TranslateIrKeyDesk getInstance() {
        if (keyinstance == null) {
            keyinstance = new TranslateIrKeyDesk();
        }
        return keyinstance;
    }

    public int translateIRKey(int key) {
        int value = key;
        int keymapsize = this.mstar_ir_keymap.length;
        for (int i = 0; i < keymapsize; i++) {
            if (key == this.mstar_ir_keymap[i][0]) {
                return this.mstar_ir_keymap[i][1];
            }
        }
        return value;
    }
}
