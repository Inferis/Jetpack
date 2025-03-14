package snekker.jetpack.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RechargerBlock extends Block implements BlockEntityProvider, BlockEntityTicker<RechargerBlockEntity> {
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public static BooleanProperty HAS_JETPACK = BooleanProperty.of("has_jetpack");

    public RechargerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(HAS_JETPACK, false)
                .with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HAS_JETPACK);
        builder.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RechargerBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return super.onUse(state, world, pos, player, hit);
        }

        if (world.getBlockEntity(pos) instanceof RechargerBlockEntity rechargerBlockEntity) {
            player.openHandledScreen(rechargerBlockEntity);
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, net.minecraft.block.BlockState state, BlockEntityType<T> type) {
        if (type == JetpackBlockEntityTypes.RECHARGER) {
            return (BlockEntityTicker<T>) this;
        }
        else {
            return null;
        }
    }

    @Override
    public void tick(World world, BlockPos pos, BlockState state, RechargerBlockEntity blockEntity) {
        blockEntity.tick(world, pos, state);
    }

    @Override
    protected void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (world.getBlockEntity(pos) instanceof RechargerBlockEntity rechargerBlockEntity) {
            var stack = rechargerBlockEntity.getJetpackSlotStack();
            if (!stack.isEmpty()) {
                var inventory = new SimpleInventory(stack);
                ItemScatterer.spawn(world, pos, inventory);
            }
        }

        super.onBlockBreakStart(state, world, pos, player);
    }
}
