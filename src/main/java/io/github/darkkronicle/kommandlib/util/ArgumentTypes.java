package io.github.darkkronicle.kommandlib.util;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.command.argument.serialize.DoubleArgumentSerializer;
import net.minecraft.command.argument.serialize.FloatArgumentSerializer;
import net.minecraft.command.argument.serialize.IntegerArgumentSerializer;
import net.minecraft.command.argument.serialize.LongArgumentSerializer;
import net.minecraft.command.argument.serialize.StringArgumentSerializer;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class ArgumentTypes {

    private static final ArgumentTypes INSTANCE = new ArgumentTypes();

    private final Map<Identifier, Entry<?>> idMap = Maps.newHashMap();
    private final Map<Class<?>, Entry<?>> classMap = Maps.newHashMap();

    public static ArgumentTypes getInstance() {
        return INSTANCE;
    }

    public <T extends ArgumentType<?>> void register(Identifier identifier, ArgumentSerializer<T> serializer, Class<T> clazz) {
        Entry<T> entry = new Entry<>(clazz, serializer, identifier);
        register(entry);
    }

    public void register(Entry<?> entry) {
        idMap.put(entry.id, entry);
        classMap.put(entry.argClass, entry);
    }

    public <T extends ArgumentType<?>> Optional<Entry<T>> get(Identifier id) {
        Entry<?> optional = idMap.get(id);
        if (optional == null) {
            return Optional.empty();
        }
        return Optional.of((Entry<T>) optional);
    }

    public <T extends ArgumentType<?>> Optional<Entry<T>> get(Class<T> clazz) {
        Entry<?> optional = classMap.get(clazz);
        if (optional == null) {
            return Optional.empty();
        }
        return Optional.of((Entry<T>) optional);
    }

    private ArgumentTypes() {
        register(new Identifier("brigadier:bool"), new ConstantArgumentSerializer(BoolArgumentType::bool), BoolArgumentType.class);
        register(new Identifier("brigadier:float"), new FloatArgumentSerializer(), FloatArgumentType.class);
        register(new Identifier("brigadier:double"), new DoubleArgumentSerializer(), DoubleArgumentType.class);
        register(new Identifier("brigadier:integer"), new IntegerArgumentSerializer(), IntegerArgumentType.class);
        register(new Identifier("brigadier:long"), new LongArgumentSerializer(), LongArgumentType.class);
        register(new Identifier("brigadier:string"), new StringArgumentSerializer(), StringArgumentType.class);
    }

    public static class Entry<T extends ArgumentType<?>> {
        public final Class<T> argClass;
        public final ArgumentSerializer<T> serializer;
        public final Identifier id;

        Entry(Class<T> argClass, ArgumentSerializer<T> serializer, Identifier id) {
            this.argClass = argClass;
            this.serializer = serializer;
            this.id = id;
        }
    }

}
