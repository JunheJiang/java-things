package com.cmg.java.algorithm算法.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 字典树
 */
public class Trie {
    private TrieNode root;


    public Trie() {
        root = new TrieNode();
        root.val = ' ';
    }


    public class TrieNode {
        public char val;
        public boolean visited;
        public TrieNode[] children = new TrieNode[26];

        public TrieNode() {
        }

        public TrieNode(char c) {
            TrieNode node = new TrieNode();
            node.val = c;
        }
    }

    private boolean search(String word) {
        TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (ws.children[c - 'a'] == null) return false;
            ws = ws.children[c - 'a'];
        }
        ws.visited = true;
        return ws.visited;
    }

    public boolean startWith(String word) {
        TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (ws.children[c - 'a'] == null) return false;
            ws = ws.children[c - 'a'];
        }
        return true;
    }

    private void insert(String word) {
        TrieNode ws = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (ws.children[c - 'a'] == null) {
                ws.children[c - 'a'] = new TrieNode(c);
            }
            ws = ws.children[c - 'a'];
        }
        ws.visited = true;
    }


    public class Solution {
        Set<String> res = new HashSet<>();

        public List<String> findWords(char[][] board, String[] words) {
            Trie trie = new Trie();
            for (String word : words) {
                trie.insert(word);
            }
            int m = board.length;
            int n = board[0].length;
            boolean[][] visited = new boolean[m][n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    dfs(board, visited, "", i, j, trie);
                }
            }
            return new ArrayList<>(res);
        }

        private void dfs(char[][] board, boolean[][] visited, String str, int x, int y, Trie trie) {
            if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) return;
            if (visited[x][y]) return;
            str += board[x][y];
            if (!trie.search(str)) return;
            if (trie.search(str)) {
                res.add(str);
            }
            visited[x][y] = true;
            dfs(board, visited, str, x - 1, y, trie);
            dfs(board, visited, str, x + 1, y, trie);
            dfs(board, visited, str, x, y - 1, trie);
            dfs(board, visited, str, x, y + 1, trie);
            visited[x][y] = false;
        }

    }
}
