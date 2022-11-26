package com.royalswans.minesweeper.sweeperfield;

import com.royalswans.minesweeper.sweeperfield.runnables.Celebrate;
import com.royalswans.minesweeper.sweeperfield.runnables.LightTNT;
import com.royalswans.minesweeper.sweeperfield.runnables.Reset;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class FieldEvents implements Listener {
    private final JavaPlugin plugin;
    private final Rectangle rect;
    private final Field field;
    private final World world;
    private final HashMap<Character, Material> blockMap = new HashMap<>();
    private ArrayList<ArrayList<Character>> fieldArr;
    private int freeSquareCount;
    private boolean isFirstClick = true;
    private boolean isOnReset = false;


    public FieldEvents(JavaPlugin plugin, int startX, int startZ, int endX, int endZ, int y, World world) {
        this.plugin = plugin;

        this.rect = new Rectangle(startX, startZ, endX - startX, endZ - startZ);
        this.field = new Field(startX, startZ, endX, endZ, y);

        this.world = world;

        blockMap.put('*', Material.TNT);
        blockMap.put('0', Material.WHITE_CONCRETE_POWDER);
        blockMap.put('1', Material.LIGHT_BLUE_CONCRETE);
        blockMap.put('2', Material.GREEN_CONCRETE);
        blockMap.put('3', Material.RED_CONCRETE);
        blockMap.put('4', Material.PURPLE_CONCRETE);
        blockMap.put('5', Material.YELLOW_CONCRETE);
        blockMap.put('6', Material.PINK_CONCRETE);
        blockMap.put('7', Material.ORANGE_CONCRETE);
        blockMap.put('8', Material.BLUE_CONCRETE);

        this.fieldArr = field.generateField();
        freeSquareCount = field.getFreeSquares(fieldArr);

        BuildField.buildField(rect, field, world);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (!Objects.requireNonNull(e.getHand()).equals(EquipmentSlot.HAND)) {
            e.setCancelled(true);
            return;
        }

        if (e.getClickedBlock() != null) {
            Block clickedBlock = e.getClickedBlock();
            Location blockLoc = clickedBlock.getLocation();

            Player player = e.getPlayer();

            int x = clickedBlock.getX();
            int y = clickedBlock.getY();
            int z = clickedBlock.getZ();

            if (rect.contains(x, z) && player.getWorld().equals(this.world)) {

                if (blockMap.containsValue(clickedBlock.getType())) {
                    e.setCancelled(true);
                    return;
                }

                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

                    if (
                            blockLoc.getBlock().getType().equals(Material.SOUL_TORCH)
                            || clickedBlock.getType().equals(Material.SOUL_TORCH)
                            || blockLoc.add(0, 1, 0).getBlock().getType().equals(Material.REDSTONE_TORCH)
                            || clickedBlock.getType().equals(Material.REDSTONE_TORCH)
                    ) {
                        spawnTorchParticles(world, blockLoc);
                        return;
                    }

                    if (player.getInventory().getItemInMainHand().getType().equals(Material.REDSTONE_TORCH)) {
                        return;
                    }

                    if (player.getInventory().getItemInMainHand().getType().equals(Material.SOUL_TORCH)) {
                        player.sendMessage(ChatColor.BLUE + "YOU HAVE PLACED THE FORBIDDEN SUS TORCH!\nNothing happens but ummm... Don't do that");
                        return;
                    }

                    e.setCancelled(true);

                    if (y != field.getY() || isOnReset) {
                        e.setCancelled(true);
                        return;
                    }

                    int squareX = x - (int) rect.getMinX();
                    int squareZ = z - (int) rect.getMinY();  // y in rect is z in game

                    if (isFirstClick) {
                        while (fieldArr.get(squareZ).get(squareX) != '0') {
                            fieldArr = field.generateField();
                            freeSquareCount = field.getFreeSquares(fieldArr);
                        }

                        isFirstClick = false;

                        revealZero(squareX, squareZ, world);
                    } else if (fieldArr.get(squareZ).get(squareX) == '0') {
                        revealZero(squareX, squareZ, world);
                    } else if (fieldArr.get(squareZ).get(squareX) == '*') {
                        world.spawnParticle(
                                Particle.BLOCK_CRACK,
                                blockLoc.add(0.5, 0, 0.5),
                                10,
                                blockMap.get('*').createBlockData());
                        world.playSound(blockLoc, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 10f);

                        clickedBlock.setType(blockMap.get('*'));

                        new LightTNT(clickedBlock).runTaskLater(plugin, 30);
                        new Reset(this, world).runTaskLater(plugin, 110);

                        isOnReset = true;

                        player.sendMessage(ChatColor.RED + "Uh oh, you hit a mine!\nResetting board...");
                    } else {
                        char fieldSquare = fieldArr.get(squareZ).get(squareX);

                        world.spawnParticle(
                                Particle.BLOCK_CRACK,
                                blockLoc.add(0.5, 0, 0.5),
                                10,
                                blockMap.get(fieldSquare).createBlockData());
                        world.playSound(blockLoc, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 10f);

                        clickedBlock.setType(blockMap.get(fieldSquare));

                        freeSquareCount--;
                    }

                    if (freeSquareCount <= 0) {
                        new Celebrate(this, world).runTaskLater(plugin, 80);

                        isOnReset = true;

                        player.sendMessage(ChatColor.GREEN + "Congratulations! You won a game of Mine Sweeper!");
                    }
                } else if (e.getAction().equals(Action.LEFT_CLICK_BLOCK)
                        && !clickedBlock.getType().equals(Material.REDSTONE_TORCH)
                        && !clickedBlock.getType().equals(Material.SOUL_TORCH)) {
                    e.setCancelled(true);

                    world.spawnParticle(
                            Particle.BLOCK_CRACK, blockLoc.add(0.5, 0, 0.5),
                            10,
                            Material.REDSTONE_TORCH.createBlockData());
                    world.playSound(blockLoc, Sound.BLOCK_WOOD_BREAK, 1f, 10f);

                    blockLoc.add(0, 1, 0).getBlock().setType(Material.REDSTONE_TORCH);
                }
            }
        }
    }

    @EventHandler
    public void onSandFall(EntityChangeBlockEvent event) {
        Block block = event.getBlock();

        int x = block.getX();
        int z = block.getZ();

        Bukkit.broadcast("2", "");

        if (rect.contains(x, z) && block.getWorld().equals(this.world)) {
            event.setCancelled(true);
            Bukkit.broadcast("1", "");
        }
    }

    private void spawnTorchParticles(World world, Location blockLoc) {
        world.spawnParticle(
                Particle.BLOCK_CRACK,
                blockLoc.add(0.5, 0, 0.5),
                10,
                Material.REDSTONE_TORCH.createBlockData());
        world.playSound(blockLoc, Sound.BLOCK_WOOD_BREAK, 1f, 1f);

        blockLoc.getBlock().setType(Material.AIR);
    }

    private void revealZero(int x, int z, World world) {
        ArrayList<Character> squareArr;

        int rectX = (int) rect.getMinX();
        int rectZ = (int) rect.getMinY();  // y in rect is z in game

        int squareStartX = Math.max(x - 1, 0);
        int squareStartZ = Math.max(z - 1, 0);
        int squareEndX = Math.min(x + 1, field.getWidth() - 1);
        int squareEndZ = Math.min(z + 1, field.getHeight() - 1);

        int blockX;
        int fieldY = field.getY();
        int blockZ;

        Block block = world.getBlockAt(rectX + x, fieldY, rectZ + z);
        Location blockLoc = block.getLocation();
        Material blockType = blockMap.get('0');

        world.spawnParticle(
                Particle.BLOCK_CRACK,
                blockLoc.add(0.5, 1, 0.5),
                10,
                blockType.createBlockData());
        world.playSound(blockLoc, Sound.BLOCK_STONE_BREAK, 1f, 10f);

        block.setType(blockMap.get('0'));
        blockLoc.getBlock().setType(Material.AIR);

        freeSquareCount--;

        for (int squareZ = squareStartZ; squareZ <= squareEndZ; squareZ++) {
            squareArr = fieldArr.get(squareZ);
            for (int squareX = squareStartX; squareX <= squareEndX; squareX++) {
                blockX = (int) rect.getMinX() + squareX;
                blockZ = (int) rect.getMinY() + squareZ;  // y in rect is z in game

                Block squareBlock = world.getBlockAt(blockX, fieldY, blockZ);
                Location squareBlockLoc = squareBlock.getLocation();

                if (blockMap.containsValue(squareBlock.getType())) {
                    continue;
                }

                if (squareArr.get(squareX) == '0') {
                    revealZero(squareX, squareZ, world);
                } else {
                    Material squareBlockType = blockMap.get(squareArr.get(squareX));

                    world.spawnParticle(
                            Particle.BLOCK_CRACK,
                            squareBlockLoc.add(0.5, 1, 0.5),
                            10,
                            squareBlockType.createBlockData());
                    world.playSound(squareBlockLoc, Sound.BLOCK_STONE_BREAK, 1f, 10f);

                    squareBlock.setType(squareBlockType);
                    squareBlockLoc.add(0, 0.5, 0).getBlock().setType(Material.AIR);

                    freeSquareCount--;
                }
            }
        }
    }

    public void resetField(World world) {
        BuildField.buildField(rect, field, world);

        fieldArr = field.generateField();
        freeSquareCount = field.getFreeSquares(fieldArr);

        isFirstClick = true;
        isOnReset = false;
    }

    public void celebrateVictory(World world) {
        for (int x = (int) rect.getMinX(); x < rect.getMaxX(); x++) {
            for (int z = (int) rect.getMinY(); z < rect.getMaxY(); z++) {  // y in rect is z in game
                world.getBlockAt(x, field.getY() + 1, z).setType(Material.AIR);

                Block block = world.getBlockAt(x, field.getY(), z);
                block.setType(Material.STONE);

                world.spawn(block.getLocation(), Firework.class);

                fieldArr = field.generateField();
                freeSquareCount = field.getFreeSquares(fieldArr);
            }
        }
        isFirstClick = true;
        isOnReset = false;
    }
}
