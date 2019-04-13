package alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class StreakBST {
    public Node root;
    public int depth;

    public StreakBST() { }

    public void Insert(int streakMin, int streakMax, Node n, Node parent) {
        if (n == null) {
            n = new Node(streakMin, streakMax, parent);

            if (parent != null) {
                if (parent.min > streakMax) {
                    parent.left = n;
                } else {
                    parent.right = n;
                }
            }
            if (root == null) {
                this.root = n;
            }

        } else if (n.min <= streakMin && streakMax <= n.max) {
            return;
        } else if (streakMax < n.min - 1) {
            Insert(streakMin, streakMax, n.left, n);
        } else if (n.max + 1 < streakMin) {
            Insert(streakMin, streakMax, n.right, n);
        } else {
            if (streakMin < n.min) {
                Delete(streakMin, n.min - 1, n.left);
                n.min = streakMin;
                LeftMerge(n);
            }
            if (n.max < streakMax) {
                Delete(n.max - 1, streakMax, n.right);
                n.max = streakMax;
                RightMerge(n);
            }
        }
    }

    public void Delete(Node n) {
        if (n.left == null && n.right == null) {
            // leaf part
            if (n.parent != null) {
                if (n.parent.left == n) {
                    n.parent.left = null;
                } else {
                    n.parent.right = null;
                }
            } else {
                if (this.root == n) {
                    this.root = null;
                }
            }
        } else if (n.left == null) {
            // right leaf only
            if (n.parent != null) {
                n.right.parent = n.parent;
                if (n.parent.left == n) {
                    n.parent.left = n.right;
                } else {
                    n.parent.right = n.right;
                }
            } else {
                if (this.root == n) {
                    this.root = n.right;
                }

                n.right.parent = null;
            }
        } else if (n.right == null) {
            // left leaf oly
            if (n.parent != null) {
                n.left.parent = n.parent;
                if (n.parent.left == n) {
                    n.parent.left = n.left;
                } else {
                    n.parent.right = n.left;
                }
            } else {
                if (this.root == n) {
                    this.root = n.left;
                }

                n.left.parent = null;
            }
        } else {
            // both leaves
            Node leftMax = GetTreeMax(n.left);

            if(leftMax.parent.left == leftMax) {
                leftMax.parent.left = null;
            } else {
                leftMax.parent.right = null;
            }

            leftMax.parent = n.parent;

            if (n.parent == null) {
                this.root = leftMax;
            }

            if (n.right != null) {
                n.right.parent = leftMax;
            }

            if (n.left != null) {
                n.left.parent = leftMax;
            }

            leftMax.left = n.left;
            leftMax.right = n.right;
        }
    }

    public void DeleteSubtree(Node n) {
        if (n == null)
            return;

        if (n.parent != null) {
            if (n.parent.left == n) {
                n.parent.left = null;
            } else {
                n.parent.right = null;
            }
        } else {
            this.root = null;
        }

        if (n.left != null) {
            n.left.parent = null;
        }

        if (n.right != null) {
            n.right.parent = null;
        }
    }

    public void TrimTree(Node n) {
        if (n.parent != null) {
            if (n.parent.left == n) {
                n.parent.left = null;
            } else {
                n.parent.right = null;
            }

            if(n.left != null) {
                n.left.parent = null;
                n.left = null;
            }
            if(n.right != null) {
                n.right.parent = null;
                n.right = null;
            }
        }
    }

    public void Delete(int streakMin, int streakMax, Node n) {
        if (n == null) {
            //System.out.println("0");
            return;
        }

        // Continue left or right:
        if (streakMax < n.min) {
            //System.out.println("1L");
            Delete(streakMin, streakMax, n.left);
        } else if (n.max < streakMin) {
            //System.out.println("1R");
            Delete(streakMin, streakMax, n.right);
        }
        //Delete inside node:
        else if (streakMax < n.max && (streakMin == n.min || streakMin == (n.min - 1))) {
            //System.out.println("2L");
            n.min = streakMax + 1;
        } else if (n.min < streakMin && (streakMax == n.max || streakMax == (n.max + 1))) {
            //System.out.println("2R");
            n.max = streakMin - 1;
        }
        //Delete inside node and continue deleting L/R:
        else if (streakMin < n.min - 1 && streakMax < n.max) {
            //System.out.println("3L");
            int min = n.min;
            n.min = streakMax + 1;
            Delete(streakMin, min - 2, n.left);
        } else if (n.min < streakMin && n.max + 1 < streakMax) {
            //System.out.println("3R");
            int max = n.max;
            n.max = streakMin - 1;
            Delete(max + 2, streakMax, n.right);
        }
        //Delete inside node and move part of node keys to L/R subtree:
        else if (n.min < streakMin && streakMax < n.max) {
            if (streakMin - n.min <= n.max - streakMax) {
                //System.out.println("4L");
                int min = n.min;
                n.min = streakMax + 1;
                Insert(min, streakMin - 1, n.left, n);
            } else if (streakMin - n.min > n.max - streakMax) {
                //System.out.println("4R");
                int max = n.max;
                n.max = streakMin - 1;
                Insert(streakMax + 1, max, n.right, n);
            }
        } else {
            Node treeMin = GetTreeMin(n);
            Node treeMax = GetTreeMax(n);
            // Delete the whole subtree rooted in the node
            if (streakMin <= treeMin.min && treeMax.max <= streakMax) {
                //System.out.println("5");
                if (this.root == n) {
                    this.root = null;
                } else {
                    DeleteSubtree(n);
                }
            } else if (streakMin <= treeMin.min && streakMax < treeMax.max) {
                //System.out.println("6L");
                DeleteSubtree(n.left);
                Delete(n.max, streakMax, n.right);

                if (root == null) {
                    root = n;
                }

                Delete(n);
            } else if (treeMin.min < streakMin && treeMax.max <= streakMax) {
                //System.out.println("6R");
                DeleteSubtree(n.right);
                Delete(streakMin, n.min, n.left);

                if (root == null) {
                    root = n;
                }

                Delete(n);
            } else if (treeMin.min < streakMin && streakMax < treeMax.max) {
                //System.out.println("7");

                LeftTrim(n, streakMin);

                // Locate node y in n.L.tree containing n.L.tree.max.
                // Delete all keys in n, copy all keys from y to n and Delete(y)

                Node y = GetTreeMax(n.left);
                n.min = y.min;
                n.max = y.max;
                Delete(y);

                RightTrim(n, streakMax);

                if (root == null) {
                    root = n;
                }
            }
        }
    }

    public void NullParent(Node p) {
        if (p.parent != null) {
            if (p.parent.right == p) {
                p.parent.right = null;
                p.parent = null;
            } else if (p.parent.left == p) {
                p.parent.left = null;
                p.parent = null;
            }
        }
    }
    
    public void LeftTrim(Node n, int val) {
        Node iteratorNode = n;

        ArrayList<Node> newRoots = new ArrayList<>();
        boolean rooted = false;

        iteratorNode = n.left;

        while (iteratorNode != null) {
            if (val <= iteratorNode.min) {
                Node backup = iteratorNode.left;
                TrimTree(iteratorNode);
                iteratorNode = backup;
                rooted = false;
            } else {
                // is root, move on
                if (!rooted) {
                    newRoots.add(iteratorNode);
                    rooted = true;

                    NullParent(iteratorNode);
                }

                iteratorNode = iteratorNode.right;
            }
        }

        Node mostRight;
        for (int i = 0; i < newRoots.size() - 1; i++) {
            mostRight = GetTreeMax(newRoots.get(i));
            mostRight.right = newRoots.get(i + 1);
            newRoots.get(i + 1).parent = mostRight;
        }

        mostRight = GetTreeMax(newRoots.get(newRoots.size() - 1));
        if (mostRight.max >= val) {
            mostRight.max = val - 1;
        }

        n.left = newRoots.get(0);
        newRoots.get(0).parent = n;
    }

    public void RightTrim(Node n, int val) {
        Node iteratorNode = n;

        ArrayList<Node> newRoots = new ArrayList<>();
        boolean rooted = false;

        iteratorNode = n.right;

        while (iteratorNode != null) {
            if (iteratorNode.max <= val) {
                Node backup = iteratorNode.right;
                TrimTree(iteratorNode);
                iteratorNode = backup;
                rooted = false;
            } else {
                // is root, move oinputNode
                if (!rooted) {
                    newRoots.add(iteratorNode);
                    rooted = true;

                    NullParent(iteratorNode);
                }

                iteratorNode = iteratorNode.left;
            }
        }

        Node mostLeft;
        for (int i = 0; i < newRoots.size() - 1; i++) {
            mostLeft = GetTreeMin(newRoots.get(i));
            mostLeft.left = newRoots.get(i + 1);
            newRoots.get(i + 1).parent = mostLeft;
        }

        mostLeft = GetTreeMin(newRoots.get(newRoots.size() - 1));
        if (mostLeft.min <= val) {
            mostLeft.min = val + 1;
        }

        n.right = newRoots.get(0);
        newRoots.get(0).parent = n;
    }

    public void LeftMerge(Node n) {
        if (n.left != null) {
            // find node with max value
            Node p = GetTreeMax(n.left);

            if (p.max + 1 == n.min) {
                n.min = p.min;
                Delete(p);
            }
        }
    }

    public void RightMerge(Node n) {
        if (n.right != null) {
            // find node with min value
            Node p = GetTreeMin(n.right);

            if (n.max + 1 == p.min) {
                n.max = p.max;
                Delete(p);
            }
        }
    }

    public Node GetTreeMax(Node n) {
        while (n.right != null) {
            n = n.right;
        }
        return n;
    }

    public Node GetTreeMin(Node n) {
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    public int GetDepth(Node n) {
        if (n == null) {
            return 0;
        } else {
            int l = GetDepth(n.left);
            int r = GetDepth(n.right);

            return Math.max(l, r) + 1;
        }
    }

    public int[] GetLevelNodeCount(Node n) {
        if (n == null) {
            return new int[] {0};
        }

        int[] counts = new int[this.depth + 1];
        Arrays.fill(counts, 0);
        Queue<Node> nodes = new LinkedList<>();

        nodes.add(this.root);
        this.root.level = 0;

        Node temp;
        while (!nodes.isEmpty()) {
            temp = nodes.poll();

            counts[temp.level] += temp.max - temp.min + 1;

            if (temp.left != null) {
                temp.left.level = temp.level + 1;
                nodes.add(temp.left);
            }

            if (temp.right != null) {
                temp.right.level = temp.level + 1;
                nodes.add(temp.right);
            }
        }

        return counts;
    }
}