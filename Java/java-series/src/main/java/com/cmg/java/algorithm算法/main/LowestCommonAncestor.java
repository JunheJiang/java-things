package com.cmg.java.algorithm算法.main;

import lombok.Data;

/**
 * 最近公共祖先
 */
public class LowestCommonAncestor {
    //递归
    TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        return left == null ? right : right == null ? left : root;
    }

    @Data
    public static class TreeNode {
        TreeNode left;
        TreeNode right;
        int val;
    }
}
