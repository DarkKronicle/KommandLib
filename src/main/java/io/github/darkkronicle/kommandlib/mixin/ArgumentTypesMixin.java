package io.github.darkkronicle.kommandlib.mixin;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(ArgumentTypes.class)
public class ArgumentTypesMixin {

    @Inject(
            method = "register(Ljava/lang/String;Ljava/lang/Class;Lnet/minecraft/command/argument/serialize/ArgumentSerializer;)V",
            at = @At("RETURN")
    )
    private static <T extends ArgumentType<?>> void register(String id, Class<T> argClass, ArgumentSerializer<T> serializer, CallbackInfo ci) {
        io.github.darkkronicle.kommandlib.util.ArgumentTypes.getInstance().register(new Identifier(id), serializer, argClass);
    }

}
