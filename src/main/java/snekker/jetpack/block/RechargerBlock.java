package snekker.jetpack.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import snekker.jetpack.Jetpack;

public class RechargerBlock extends Block implements BlockEntityProvider, BlockEntityTicker<RechargerBlockEntity> {
    private static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    public static BooleanProperty HAS_JETPACK = BooleanProperty.of("has_jetpack");

    public RechargerBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(HAS_JETPACK, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HAS_JETPACK);
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
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return super.onUse(state, world, pos, player, hit);
        }

        if (world.getBlockEntity(pos) instanceof RechargerBlockEntity rechargerBlockEntity) {
            player.openHandledScreen(rechargerBlockEntity);
        }

        world.setBlockState(pos, state.with(HAS_JETPACK, true));

        return ActionResult.SUCCESS;
    }

    @Override
    protected @Nullable NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return (RechargerBlockEntity)world.getBlockEntity(pos);
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
}
