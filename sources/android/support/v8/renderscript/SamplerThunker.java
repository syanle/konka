package android.support.v8.renderscript;

import android.renderscript.BaseObj;
import android.renderscript.Sampler;
import android.support.v8.renderscript.Sampler.Value;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;

class SamplerThunker extends Sampler {
    Sampler mN;

    public static class Builder {
        float mAniso;
        Value mMag = Value.NEAREST;
        Value mMin = Value.NEAREST;
        RenderScriptThunker mRS;
        Value mWrapR = Value.WRAP;
        Value mWrapS = Value.WRAP;
        Value mWrapT = Value.WRAP;

        public Builder(RenderScriptThunker rs) {
            this.mRS = rs;
        }

        public void setMinification(Value v) {
            if (v == Value.NEAREST || v == Value.LINEAR || v == Value.LINEAR_MIP_LINEAR || v == Value.LINEAR_MIP_NEAREST) {
                this.mMin = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public void setMagnification(Value v) {
            if (v == Value.NEAREST || v == Value.LINEAR) {
                this.mMag = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public void setWrapS(Value v) {
            if (v == Value.WRAP || v == Value.CLAMP || v == Value.MIRRORED_REPEAT) {
                this.mWrapS = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public void setWrapT(Value v) {
            if (v == Value.WRAP || v == Value.CLAMP || v == Value.MIRRORED_REPEAT) {
                this.mWrapT = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public void setAnisotropy(float v) {
            if (v >= FlyingIcon.ANGULAR_VMIN) {
                this.mAniso = v;
                return;
            }
            throw new IllegalArgumentException("Invalid value");
        }

        public Sampler create() {
            this.mRS.validate();
            android.renderscript.Sampler.Builder b = new android.renderscript.Sampler.Builder(this.mRS.mN);
            b.setMinification(SamplerThunker.convertValue(this.mMin));
            b.setMagnification(SamplerThunker.convertValue(this.mMag));
            b.setWrapS(SamplerThunker.convertValue(this.mWrapS));
            b.setWrapT(SamplerThunker.convertValue(this.mWrapT));
            b.setAnisotropy(this.mAniso);
            Sampler s = b.create();
            SamplerThunker sampler = new SamplerThunker(0, this.mRS);
            sampler.mMin = this.mMin;
            sampler.mMag = this.mMag;
            sampler.mWrapS = this.mWrapS;
            sampler.mWrapT = this.mWrapT;
            sampler.mWrapR = this.mWrapR;
            sampler.mAniso = this.mAniso;
            sampler.mN = s;
            return sampler;
        }
    }

    protected SamplerThunker(int id, RenderScript rs) {
        super(id, rs);
    }

    /* access modifiers changed from: 0000 */
    public BaseObj getNObj() {
        return this.mN;
    }

    static Sampler.Value convertValue(Value v) {
        switch (v) {
            case NEAREST:
                return Sampler.Value.NEAREST;
            case LINEAR:
                return Sampler.Value.LINEAR;
            case LINEAR_MIP_LINEAR:
                return Sampler.Value.LINEAR_MIP_LINEAR;
            case LINEAR_MIP_NEAREST:
                return Sampler.Value.LINEAR_MIP_NEAREST;
            case WRAP:
                return Sampler.Value.WRAP;
            case CLAMP:
                return Sampler.Value.CLAMP;
            case MIRRORED_REPEAT:
                return Sampler.Value.MIRRORED_REPEAT;
            default:
                return null;
        }
    }
}
