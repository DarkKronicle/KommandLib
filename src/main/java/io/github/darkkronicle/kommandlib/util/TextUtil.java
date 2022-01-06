package io.github.darkkronicle.kommandlib.util;

import lombok.experimental.UtilityClass;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

@UtilityClass
public class TextUtil {

    public LiteralText ofFormatting(String string, Formatting formatting) {
        LiteralText text = new LiteralText(string);
        text.setStyle(Style.EMPTY.withFormatting(formatting));
        return text;
    }

    public LiteralText concat(LiteralText... texts) {
        LiteralText mutable = null;
        for (LiteralText text : texts) {
            if (mutable == null) {
                mutable = text;
            } else {
                mutable.append(text);
            }
        }
        return mutable;
    }

    public LiteralText concat(LiteralText text, String string) {
        return (LiteralText) text.append(string);
    }

}
