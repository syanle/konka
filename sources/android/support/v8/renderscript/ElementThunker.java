package android.support.v8.renderscript;

import android.renderscript.Element;
import android.renderscript.Element.Builder;
import android.support.v8.renderscript.Element.DataKind;
import android.support.v8.renderscript.Element.DataType;
import com.cyanogenmod.trebuchet.RocketLauncher.Board;
import com.konka.kkinterface.tv.DtvInterface.EPG_EVENT_INFO;
import com.umeng.common.util.g;

class ElementThunker extends Element {
    Element mN;

    /* renamed from: android.support.v8.renderscript.ElementThunker$1 reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$support$v8$renderscript$Element$DataType = new int[DataType.values().length];

        static {
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.NONE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.FLOAT_32.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.FLOAT_64.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.SIGNED_8.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.SIGNED_16.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.SIGNED_32.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.SIGNED_64.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.UNSIGNED_8.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.UNSIGNED_16.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.UNSIGNED_32.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.UNSIGNED_64.ordinal()] = 11;
            } catch (NoSuchFieldError e11) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.BOOLEAN.ordinal()] = 12;
            } catch (NoSuchFieldError e12) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.MATRIX_4X4.ordinal()] = 13;
            } catch (NoSuchFieldError e13) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.MATRIX_3X3.ordinal()] = 14;
            } catch (NoSuchFieldError e14) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.MATRIX_2X2.ordinal()] = 15;
            } catch (NoSuchFieldError e15) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.RS_ELEMENT.ordinal()] = 16;
            } catch (NoSuchFieldError e16) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.RS_TYPE.ordinal()] = 17;
            } catch (NoSuchFieldError e17) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.RS_ALLOCATION.ordinal()] = 18;
            } catch (NoSuchFieldError e18) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.RS_SAMPLER.ordinal()] = 19;
            } catch (NoSuchFieldError e19) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataType[DataType.RS_SCRIPT.ordinal()] = 20;
            } catch (NoSuchFieldError e20) {
            }
            $SwitchMap$android$support$v8$renderscript$Element$DataKind = new int[DataKind.values().length];
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataKind[DataKind.USER.ordinal()] = 1;
            } catch (NoSuchFieldError e21) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataKind[DataKind.PIXEL_L.ordinal()] = 2;
            } catch (NoSuchFieldError e22) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataKind[DataKind.PIXEL_A.ordinal()] = 3;
            } catch (NoSuchFieldError e23) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataKind[DataKind.PIXEL_LA.ordinal()] = 4;
            } catch (NoSuchFieldError e24) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataKind[DataKind.PIXEL_RGB.ordinal()] = 5;
            } catch (NoSuchFieldError e25) {
            }
            try {
                $SwitchMap$android$support$v8$renderscript$Element$DataKind[DataKind.PIXEL_RGBA.ordinal()] = 6;
            } catch (NoSuchFieldError e26) {
            }
        }
    }

    static class BuilderThunker {
        Builder mN;

        public BuilderThunker(RenderScript rs) {
            this.mN = new Builder(((RenderScriptThunker) rs).mN);
        }

        public void add(Element e, String name, int arraySize) {
            this.mN.add(((ElementThunker) e).mN, name, arraySize);
        }

        public Element create(RenderScript rs) {
            return new ElementThunker(rs, this.mN.create());
        }
    }

    /* access modifiers changed from: 0000 */
    public Element getNObj() {
        return this.mN;
    }

    public int getBytesSize() {
        return this.mN.getBytesSize();
    }

    public int getVectorSize() {
        return this.mN.getVectorSize();
    }

    static Element.DataKind convertKind(DataKind cdk) {
        switch (cdk) {
            case USER:
                return Element.DataKind.USER;
            case PIXEL_L:
                return Element.DataKind.PIXEL_L;
            case PIXEL_A:
                return Element.DataKind.PIXEL_A;
            case PIXEL_LA:
                return Element.DataKind.PIXEL_LA;
            case PIXEL_RGB:
                return Element.DataKind.PIXEL_RGB;
            case PIXEL_RGBA:
                return Element.DataKind.PIXEL_RGBA;
            default:
                return null;
        }
    }

    static Element.DataType convertType(DataType cdt) {
        switch (AnonymousClass1.$SwitchMap$android$support$v8$renderscript$Element$DataType[cdt.ordinal()]) {
            case 1:
                return Element.DataType.NONE;
            case 2:
                return Element.DataType.FLOAT_32;
            case 3:
                return Element.DataType.FLOAT_64;
            case 4:
                return Element.DataType.SIGNED_8;
            case 5:
                return Element.DataType.SIGNED_16;
            case 6:
                return Element.DataType.SIGNED_32;
            case 7:
                return Element.DataType.SIGNED_64;
            case 8:
                return Element.DataType.UNSIGNED_8;
            case 9:
                return Element.DataType.UNSIGNED_16;
            case 10:
                return Element.DataType.UNSIGNED_32;
            case 11:
                return Element.DataType.UNSIGNED_64;
            case EPG_EVENT_INFO.EN_EPG_FUNC_STATUS_DB_NO_CHANNEL_DB /*12*/:
                return Element.DataType.BOOLEAN;
            case 13:
                return Element.DataType.MATRIX_4X4;
            case 14:
                return Element.DataType.MATRIX_3X3;
            case 15:
                return Element.DataType.MATRIX_2X2;
            case g.g /*16*/:
                return Element.DataType.RS_ELEMENT;
            case 17:
                return Element.DataType.RS_TYPE;
            case 18:
                return Element.DataType.RS_ALLOCATION;
            case 19:
                return Element.DataType.RS_SAMPLER;
            case Board.NUM_ICONS /*20*/:
                return Element.DataType.RS_SCRIPT;
            default:
                return null;
        }
    }

    public boolean isComplex() {
        return this.mN.isComplex();
    }

    public int getSubElementCount() {
        return this.mN.getSubElementCount();
    }

    public Element getSubElement(int index) {
        return new ElementThunker(this.mRS, this.mN.getSubElement(index));
    }

    public String getSubElementName(int index) {
        return this.mN.getSubElementName(index);
    }

    public int getSubElementArraySize(int index) {
        return this.mN.getSubElementArraySize(index);
    }

    public int getSubElementOffsetBytes(int index) {
        return this.mN.getSubElementOffsetBytes(index);
    }

    public DataType getDataType() {
        return this.mType;
    }

    public DataKind getDataKind() {
        return this.mKind;
    }

    ElementThunker(RenderScript rs, Element e) {
        super(0, rs);
        this.mN = e;
    }

    static Element create(RenderScript rs, DataType dt) {
        RenderScriptThunker rst = (RenderScriptThunker) rs;
        Element e = null;
        switch (AnonymousClass1.$SwitchMap$android$support$v8$renderscript$Element$DataType[dt.ordinal()]) {
            case 2:
                e = Element.F32(rst.mN);
                break;
            case 3:
                e = Element.F64(rst.mN);
                break;
            case 4:
                e = Element.I8(rst.mN);
                break;
            case 5:
                e = Element.I16(rst.mN);
                break;
            case 6:
                e = Element.I32(rst.mN);
                break;
            case 7:
                e = Element.I64(rst.mN);
                break;
            case 8:
                e = Element.U8(rst.mN);
                break;
            case 9:
                e = Element.U16(rst.mN);
                break;
            case 10:
                e = Element.U32(rst.mN);
                break;
            case 11:
                e = Element.U64(rst.mN);
                break;
            case EPG_EVENT_INFO.EN_EPG_FUNC_STATUS_DB_NO_CHANNEL_DB /*12*/:
                e = Element.BOOLEAN(rst.mN);
                break;
            case 13:
                e = Element.MATRIX_4X4(rst.mN);
                break;
            case 14:
                e = Element.MATRIX_3X3(rst.mN);
                break;
            case 15:
                e = Element.MATRIX_2X2(rst.mN);
                break;
            case g.g /*16*/:
                e = Element.ELEMENT(rst.mN);
                break;
            case 17:
                e = Element.TYPE(rst.mN);
                break;
            case 18:
                e = Element.ALLOCATION(rst.mN);
                break;
            case 19:
                e = Element.SAMPLER(rst.mN);
                break;
            case Board.NUM_ICONS /*20*/:
                e = Element.SCRIPT(rst.mN);
                break;
        }
        return new ElementThunker(rs, e);
    }

    public static Element createVector(RenderScript rs, DataType dt, int size) {
        return new ElementThunker(rs, Element.createVector(((RenderScriptThunker) rs).mN, convertType(dt), size));
    }

    public static Element createPixel(RenderScript rs, DataType dt, DataKind dk) {
        return new ElementThunker(rs, Element.createPixel(((RenderScriptThunker) rs).mN, convertType(dt), convertKind(dk)));
    }

    public boolean isCompatible(Element e) {
        return ((ElementThunker) e).mN.isCompatible(this.mN);
    }
}
