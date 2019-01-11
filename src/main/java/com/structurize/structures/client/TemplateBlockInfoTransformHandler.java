package com.structurize.structures.client;

import net.minecraft.world.gen.structure.template.Template;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class TemplateBlockInfoTransformHandler
{
    private static TemplateBlockInfoTransformHandler ourInstance = new TemplateBlockInfoTransformHandler();

    public static TemplateBlockInfoTransformHandler getInstance()
    {
        return ourInstance;
    }

    private Map<Predicate<Template.BlockInfo>, Function<Template.BlockInfo, Template.BlockInfo>> blockInfoTransformHandler = new HashMap<>();

    private TemplateBlockInfoTransformHandler()
    {
    }

    public void AddTransformHandler(@NotNull final Predicate<Template.BlockInfo> transformPredicate, @NotNull final Function<Template.BlockInfo, Template.BlockInfo> transformHandler)
    {
        blockInfoTransformHandler.put(transformPredicate, transformHandler);
    }

    public Template.BlockInfo Transform(@NotNull final Template.BlockInfo blockInfo)
    {
        return getTransformHandler(blockInfo).apply(blockInfo);
    }

    private Function<Template.BlockInfo, Template.BlockInfo> getTransformHandler(@NotNull final Template.BlockInfo blockInfo)
    {
        return blockInfoTransformHandler.keySet().stream().filter(p -> p.test(blockInfo)).findFirst().map(p -> blockInfoTransformHandler.get(p)).orElse(Function.identity());
    }
}