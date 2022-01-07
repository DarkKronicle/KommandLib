package io.github.darkkronicle.kommandlib.util;

import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.argument.AngleArgumentType;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.BlockPredicateArgumentType;
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
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.command.argument.NumberRangeArgumentType;
import net.minecraft.command.argument.OperationArgumentType;
import net.minecraft.command.argument.ParticleEffectArgumentType;
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
import java.util.function.Supplier;

public class ArgumentTypes {

    private static final ArgumentTypes INSTANCE = new ArgumentTypes();

    private final Map<Identifier, Entry<?>> idMap = Maps.newHashMap();
    private final Map<Class<?>, Entry<?>> classMap = Maps.newHashMap();

    public static ArgumentTypes getInstance() {
        return INSTANCE;
    }

    public <T extends ArgumentType<?>> void register(String identifier, Class<T> clazz, Supplier<T> supplier) {
        register(new Identifier(identifier), clazz, supplier);
    }

    public <T extends ArgumentType<?>> void register(Identifier identifier, Class<T> clazz, Supplier<T> supplier) {
        Entry<T> entry = new Entry<>(clazz, supplier, identifier);
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
        register("brigadier:bool", BoolArgumentType.class, BoolArgumentType::bool);
        register("brigadier:float", FloatArgumentType.class, FloatArgumentType::floatArg);
        register("brigadier:double", DoubleArgumentType.class, DoubleArgumentType::doubleArg);
        register("brigadier:integer", IntegerArgumentType.class, IntegerArgumentType::integer);
        register("brigadier:long", LongArgumentType.class, LongArgumentType::longArg);
        register("string", StringArgumentType.class, StringArgumentType::string);
        register("greedy", StringArgumentType.class, StringArgumentType::greedyString);
        register("entity", EntityArgumentType.class, EntityArgumentType::entity);
        register("game_profile", GameProfileArgumentType.class, GameProfileArgumentType::gameProfile);
        register("block_pos", BlockPosArgumentType.class, BlockPosArgumentType::blockPos);
        register("column_pos", ColumnPosArgumentType.class, ColumnPosArgumentType::columnPos);
        register("vec3", Vec3ArgumentType.class, Vec3ArgumentType::vec3);
        register("vec2", Vec2ArgumentType.class, Vec2ArgumentType::vec2);
        register("block_state", BlockStateArgumentType.class, BlockStateArgumentType::blockState);
        register("block_predicate", BlockPredicateArgumentType.class, BlockPredicateArgumentType::blockPredicate);
        register("item_stack", ItemStackArgumentType.class, ItemStackArgumentType::itemStack);
        register("item_predicate", ItemPredicateArgumentType.class, ItemPredicateArgumentType::itemPredicate);
        register("color", ColorArgumentType.class, ColorArgumentType::color);
        register("component", TextArgumentType.class, TextArgumentType::text);
        register("message", MessageArgumentType.class, MessageArgumentType::message);
        register("nbt_compound_tag", NbtCompoundArgumentType.class, NbtCompoundArgumentType::nbtCompound);
        register("nbt_tag", NbtElementArgumentType.class, NbtElementArgumentType::nbtElement);
        register("nbt_path", NbtPathArgumentType.class, NbtPathArgumentType::nbtPath);
        register("objective", ScoreboardObjectiveArgumentType.class, ScoreboardObjectiveArgumentType::scoreboardObjective);
        register("objective_criteria", ScoreboardCriterionArgumentType.class, ScoreboardCriterionArgumentType::scoreboardCriterion);
        register("operation", OperationArgumentType.class, OperationArgumentType::operation);
        register("particle", ParticleEffectArgumentType.class, ParticleEffectArgumentType::particleEffect);
        register("angle", AngleArgumentType.class, AngleArgumentType::angle);
        register("rotation", RotationArgumentType.class, RotationArgumentType::rotation);
        register("scoreboard_slot", ScoreboardSlotArgumentType.class, ScoreboardSlotArgumentType::scoreboardSlot);
        register("score_holder", ScoreHolderArgumentType.class, ScoreHolderArgumentType::scoreHolder);
        register("swizzle", SwizzleArgumentType.class, SwizzleArgumentType::swizzle);
        register("team", TeamArgumentType.class, TeamArgumentType::team);
        register("item_slot", ItemSlotArgumentType.class, ItemSlotArgumentType::itemSlot);
        register("resource_location", IdentifierArgumentType.class, IdentifierArgumentType::identifier);
        register("mob_effect", StatusEffectArgumentType.class, StatusEffectArgumentType::statusEffect);
        register("function", CommandFunctionArgumentType.class, CommandFunctionArgumentType::commandFunction);
        register("entity_anchor", EntityAnchorArgumentType.class, EntityAnchorArgumentType::entityAnchor);
        register("int_range", NumberRangeArgumentType.IntRangeArgumentType.class, NumberRangeArgumentType::intRange);
        register("float_range", NumberRangeArgumentType.FloatRangeArgumentType.class, NumberRangeArgumentType::floatRange);
        register("item_enchantment", EnchantmentArgumentType.class, EnchantmentArgumentType::enchantment);
        register("entity_summon", EntitySummonArgumentType.class, EntitySummonArgumentType::entitySummon);
        register("dimension", DimensionArgumentType.class, DimensionArgumentType::dimension);
        register("time", TimeArgumentType.class, TimeArgumentType::time);
        register("uuid", UuidArgumentType.class, UuidArgumentType::uuid);
    }

    public static class Entry<T extends ArgumentType<?>> {
        public final Class<T> argClass;
        public final Supplier<T> supplier;
        public final Identifier id;

        Entry(Class<T> argClass, Supplier<T> supplier, Identifier id) {
            this.argClass = argClass;
            this.supplier = supplier;
            this.id = id;
        }
    }

}
