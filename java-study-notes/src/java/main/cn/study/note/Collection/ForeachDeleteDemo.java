package cn.study.note.Collection;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jixuelei
 * @date 2020/11/24
 */
public class ForeachDeleteDemo {

    /**
     *  foreach循环里不能remove/add元素的原理
     */
    @Test
    public void t1() {
        List<String> list = new ArrayList<>(); // 1- 初始化时【modCount = 0，该变量是AbstractList的成员变量，标识该list被修改的次数】
        list.add("1");  // 2- 调用 add()方法，  ++modCount， 该值会自增1
        list.add("2");  // 3- 同上
        list.add("3");  // 3- 同上


        /**
         * 普通for循环会发生漏删的情况【a,a,b】,if(a = list[i]){list.remove(list[i])}
         *      因为普通for循环是根据索引删除的，由于两个相同值在相邻的位置，当删除第一个值之后，
         * 集合发生改变要重新排序索引（因为集合发生改变他的的底层**Object[]**要做位移操作，这里是要向前位移一个索引），
         * 所以后面那个要被删除的值就被挪到了删除值的索引位置，从而避免了删除也就造成了漏删。
         * 解决普通for循环漏删的情况：
         * 1、使用倒叙遍历，数组重排序会使得被删除的元素前移，不会影响左边的元素
         */
        ArrayList<String> strings = Lists.newArrayList("a", "a", "b");
        System.out.println(strings);
        for (int i = strings.size() - 1; i >= 0; i--) {
            if ("a".equals(strings.get(i))) {
                strings.remove(strings.get(i));
            }
        }
        System.out.println(strings);


// ------------------------------------------------------------------

        /**
         * for（T t : List<T>）{}
         * 4- 语法糖，实际上是迭代器Iterator， 内部类 Itr， 初始化时会设置expectedModCount = modCount， 每次使用.next()方法回去checkForComodification()
         *      使用ArrayList的remove/add方法会修改modCount的值，但不会改expectedModCount的值，
         *      而使用迭代器的remove方法会通过expectedModCount = modCount;修改【期望修改次数】值。
         *      4.1- 而removeIf()方法内部也会修改expectedModCount值
         *      4.2- 解决方法： 使用迭代器进行遍历，使用Itr类的remove()
         *      !!!4.3- 疑问：为何删除forEach删除集合中的倒数第二个元素就不会报错？？
         *          报错的原因是进入了next()方法，没报错的原因就是没有进入.hasNext()方法。
         *             hasNext()是判断 cursor != size; cursor是Itr内部类的成员变量表示当前迭代器的位置，而size是ArrayList的成有变量表示
         *             元素的个数，当倒数第二个满足条件后被删除，此时的size--，这样最后一个元素就被漏掉了。通过加一行打印代码查看只打印除了最后一个的元素。
         */
        for (String str : list) {
            System.out.println(str);
            if ("2".equals(str)) {
                list.remove(str);
            }
        }

        //list.removeIf("2"::equals);
        System.out.println(list);
    }
}
