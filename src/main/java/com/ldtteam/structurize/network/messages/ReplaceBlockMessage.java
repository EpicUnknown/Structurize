package com.ldtteam.structurize.network.messages;

import com.ldtteam.structurize.management.Manager;
import com.ldtteam.structurize.util.ScanToolOperation;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Message to replace a block from the world with another one.
 */
public class ReplaceBlockMessage implements IMessage
{
    /**
     * Position to scan from.
     */
    private BlockPos from;

    /**
     * Position to scan to.
     */
    private BlockPos to;

    /**
     * The block to remove from the world.
     */
    private ItemStack blockFrom;

    /**
     * The block to remove from the world.
     */
    private ItemStack blockTo;

    /**
     * Empty constructor used when registering the message.
     */
    public ReplaceBlockMessage()
    {
        super();
    }

    /**
     * Create a message to replace a block from the world.
     * @param pos1 start coordinate.
     * @param pos2 end coordinate.
     * @param blockFrom the block to replace.
     * @param blockTo the block to replace it with.
     */
    public ReplaceBlockMessage(@NotNull final BlockPos pos1, @NotNull final BlockPos pos2, @NotNull final ItemStack blockFrom, @NotNull final ItemStack blockTo)
    {
        super();
        this.from = pos1;
        this.to = pos2;
        this.blockFrom = blockFrom;
        this.blockTo = blockTo;
    }

    @Override
    public void fromBytes(@NotNull final PacketBuffer buf)
    {
        from = buf.readBlockPos();
        to = buf.readBlockPos();
        blockTo = buf.readItemStack();
        blockFrom = buf.readItemStack();
    }

    @Override
    public void toBytes(@NotNull final PacketBuffer buf)
    {
        buf.writeBlockPos(from);
        buf.writeBlockPos(to);
        buf.writeItemStack(blockTo);
        buf.writeItemStack(blockFrom);
    }

    @Nullable
    @Override
    public LogicalSide getExecutionSide()
    {
        return LogicalSide.SERVER;
    }

    @Override
    public void onExecute(final NetworkEvent.Context ctxIn, final boolean isLogicalServer)
    {
        if (!ctxIn.getSender().isCreative())
        {
            return;
        }

        Manager.addToQueue(new ScanToolOperation(ScanToolOperation.OperationType.REPLACE_BLOCK, from, to, ctxIn.getSender(), blockFrom, blockTo));
    }
}
