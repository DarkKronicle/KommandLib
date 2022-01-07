package io.github.darkkronicle.kommandlib.util;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.AngleArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.BlockPredicateArgumentType;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.command.argument.BlockStateArgumentType;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.ColumnPosArgumentType;
import net.minecraft.command.argument.CommandFunctionArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.EnchantmentArgumentType;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.ItemPredicateArgumentType;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.command.argument.ItemStackArgument;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.command.argument.NumberRangeArgumentType;
import net.minecraft.command.argument.OperationArgumentType;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.RotationArgumentType;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.command.argument.ScoreboardCriterionArgumentType;
import net.minecraft.command.argument.ScoreboardObjectiveArgumentType;
import net.minecraft.command.argument.ScoreboardSlotArgumentType;
import net.minecraft.command.argument.StatusEffectArgumentType;
import net.minecraft.command.argument.SwizzleArgumentType;
import net.minecraft.command.argument.TeamArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.command.argument.TimeArgumentType;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.command.argument.Vec2ArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class ArgumentTypes {

    private static final ArgumentTypes INSTANCE = new ArgumentTypes();

    private final Map<Identifier, Entry<?, ?>> idMap = Maps.newHashMap();
    private final Map<Class<?>, Entry<?, ?>> classMap = Maps.newHashMap();

    public static ArgumentTypes getInstance() {
        return INSTANCE;
    }

    public <S, T extends ArgumentType<S>> void register(String identifier, Class<S> valueClass, Class<T> clazz, Supplier<T> supplier, BiFunction<S, ServerCommandSource, String> stringFunction) {
        register(new Identifier(identifier), valueClass, clazz, supplier, stringFunction);
    }

    public <S, T extends ArgumentType<S>> void register(Identifier identifier, Class<S> valueClass, Class<T> clazz, Supplier<T> supplier, BiFunction<S, ServerCommandSource, String> stringFunction) {
        Entry<S, T> entry = new Entry<>(valueClass, stringFunction, clazz, supplier, identifier);
        register(entry);
    }

    public void register(Entry<?, ?> entry) {
        idMap.put(entry.id, entry);
        classMap.put(entry.argClass, entry);
    }

    public <S, T extends ArgumentType<S>> Optional<Entry<S, T>> get(Identifier id) {
        Entry<?, ?> optional = idMap.get(id);
        if (optional == null) {
            return Optional.empty();
        }
        return Optional.of((Entry<S, T>) optional);
    }

    public <S, T extends ArgumentType<S>> Optional<Entry<S, T>> get(Class<T> clazz) {
        Entry<?, ?> optional = classMap.get(clazz);
        if (optional == null) {
            return Optional.empty();
        }
        return Optional.of((Entry<S, T>) optional);
    }

    private ArgumentTypes() {
        register("brigadier:bool", Boolean.class, BoolArgumentType.class, BoolArgumentType::bool, (s, c) -> s.toString());
        register("brigadier:float", Float.class, FloatArgumentType.class, FloatArgumentType::floatArg, (s, c) -> s.toString());
        register("brigadier:double", Double.class, DoubleArgumentType.class, DoubleArgumentType::doubleArg, (s, c) -> s.toString());
        register("brigadier:integer", Integer.class, IntegerArgumentType.class, IntegerArgumentType::integer, (s, c) -> s.toString());
        register("brigadier:long", Long.class, LongArgumentType.class, LongArgumentType::longArg, (s, c) -> s.toString());
        register("string", String.class, StringArgumentType.class, StringArgumentType::string, (s, c) -> s.toString());
        register("greedy", String.class, StringArgumentType.class, StringArgumentType::greedyString, (s, c) -> s.toString());
        register("block_pos", PosArgument.class, BlockPosArgumentType.class, BlockPosArgumentType::blockPos, (s, c) -> s.toAbsoluteBlockPos(c).toShortString().replaceAll(",", ""));
        register("column_pos", PosArgument.class, ColumnPosArgumentType.class, ColumnPosArgumentType::columnPos, (s, c) -> s.toAbsoluteBlockPos(c).toShortString().replaceAll(",", ""));
        register("vec3", PosArgument.class, Vec3ArgumentType.class, Vec3ArgumentType::vec3, (s, c) -> s.toAbsoluteBlockPos(c).toShortString().replaceAll(",", ""));
        register("vec2", PosArgument.class, Vec2ArgumentType.class, Vec2ArgumentType::vec2, (s, c) -> s.toAbsoluteBlockPos(c).getX() + " " + s.toAbsoluteBlockPos(c).getZ());
        register("block_state", BlockStateArgument.class, BlockStateArgumentType.class, BlockStateArgumentType::blockState, (s, c) -> String.valueOf(Registry.BLOCK.getId(s.getBlockState().getBlock())));
//        register("block_predicate", BlockPredicateArgumentType.class, BlockPredicateArgumentType::blockPredicate);
        register("item_stack", ItemStackArgument.class, ItemStackArgumentType.class, ItemStackArgumentType::itemStack, (s, c) -> Registry.ITEM.getId(s.getItem()).toString());
//        register("item_predicate", ItemPredicateArgumentType.class, ItemPredicateArgumentType::itemPredicate);
//        register("color", ColorArgumentType.class, ColorArgumentType::color);
//        register("component", TextArgumentType.class, TextArgumentType::text);
//        register("message", MessageArgumentType.class, MessageArgumentType::message);
//        register("nbt_compound_tag", NbtCompoundArgumentType.class, NbtCompoundArgumentType::nbtCompound);
//        register("nbt_tag", NbtElementArgumentType.class, NbtElementArgumentType::nbtElement);
//        register("nbt_path", NbtPathArgumentType.class, NbtPathArgumentType::nbtPath);
        register("objective", String.class, ScoreboardObjectiveArgumentType.class, ScoreboardObjectiveArgumentType::scoreboardObjective, (s, c) -> s);
//        register("objective_criteria", ScoreboardCriterionArgumentType.class, ScoreboardCriterionArgumentType::scoreboardCriterion);
//        register("operation", OperationArgumentType.class, OperationArgumentType::operation);
//        register("particle", ParticleEffectArgumentType.class, ParticleEffectArgumentType::particleEffect);
        register("angle", AngleArgumentType.Angle.class, AngleArgumentType.class, AngleArgumentType::angle, (s, c) -> String.valueOf(s.getAngle(c)));
        register("rotation", PosArgument.class, RotationArgumentType.class, RotationArgumentType::rotation, (s, c) -> s.toAbsoluteBlockPos(c).getX() + " " + s.toAbsoluteBlockPos(c).getZ());
        register("scoreboard_slot", Integer.class, ScoreboardSlotArgumentType.class, ScoreboardSlotArgumentType::scoreboardSlot, (s, c) -> s.toString());
//        register("score_holder", ScoreHolderArgumentType.class, ScoreHolderArgumentType::scoreHolder);
//        register("swizzle", SwizzleArgumentType.class, SwizzleArgumentType::swizzle);
        register("team", String.class, TeamArgumentType.class, TeamArgumentType::team, (s, c) -> s);
        register("item_slot", Integer.class, ItemSlotArgumentType.class, ItemSlotArgumentType::itemSlot, (s, c) -> s.toString());
//        register("resource_location", IdentifierArgumentType.class, IdentifierArgumentType::identifier);
//        register("mob_effect", StatusEffectArgumentType.class, StatusEffectArgumentType::statusEffect);
//        register("function", CommandFunctionArgumentType.class, CommandFunctionArgumentType::commandFunction);
//        register("entity_anchor", EntityAnchorArgumentType.class, EntityAnchorArgumentType::entityAnchor);
//        register("int_range", NumberRangeArgumentType.IntRangeArgumentType.class, NumberRangeArgumentType::intRange);
//        register("float_range", NumberRangeArgumentType.FloatRangeArgumentType.class, NumberRangeArgumentType::floatRange);
        register("item_enchantment", Enchantment.class, EnchantmentArgumentType.class, EnchantmentArgumentType::enchantment, (s, c) -> Registry.ENCHANTMENT.getId(s).toString());
        register("entity_summon", Identifier.class, EntitySummonArgumentType.class, EntitySummonArgumentType::entitySummon, (s, c) -> s.toString());
        register("dimension", Identifier.class, DimensionArgumentType.class, DimensionArgumentType::dimension, (s, c) -> s.toString());
        register("time", Integer.class, TimeArgumentType.class, TimeArgumentType::time, (s, c) -> s.toString());
        register("uuid", UUID.class, UuidArgumentType.class, UuidArgumentType::uuid, (s, c) -> s.toString());
    }

    public static class Entry<S, T extends ArgumentType<S>> {
        public final Class<S> valueClass;
        public final Class<T> argClass;
        public final Supplier<T> supplier;
        public final BiFunction<S, ServerCommandSource, String> stringFunction;
        public final Identifier id;

        public Entry(Class<S> valueClass, BiFunction<S, ServerCommandSource, String> stringFunction, Class<T> argClass, Supplier<T> supplier, Identifier id) {
            this.valueClass = valueClass;
            this.stringFunction = stringFunction;
            this.argClass = argClass;
            this.supplier = supplier;
            this.id = id;
        }
    }

}
