package com.ling.elasticsearch;

import com.ling.elasticsearch.model.Customer;
import com.ling.elasticsearch.repository.CustomerRepository;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository repository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    //测试存储数据
    @Test
    public void saveCustomers() {
        repository.save(new Customer("Alice", "北京",13));
        repository.save(new Customer("Bob", "北京",23));
        repository.save(new Customer("neo", "西安",30));
        repository.save(new Customer("summer", "烟台",22));
        repository.save(new Customer("ling", "湖南",22));
        repository.save(new Customer("max", "山东",24));
    }

    //查找全部数据
    @Test
    public void fetchAllCustomers() {
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        Iterable<Customer> iterable=repository.findAll();
        for (Customer customer :iterable) {
            System.out.println(customer);
        }
    }

    //删除数据
    @Test
    public void deleteCustomers() {
        repository.deleteAll();
//        repository.deleteByUserName("neo");
    }

    //修改数据
    @Test
    public void updateCustomers() {
        Customer customer= repository.findByUserName("summer");
        System.out.println(customer);
        customer.setAddress("北京市海淀区西直门");
        repository.save(customer);
        Customer xcustomer=repository.findByUserName("summer");
        System.out.println(xcustomer);
    }

    //根据特定字段检索
    @Test
    public void fetchIndividualCustomers() {
        System.out.println("Customer found with findByUserName('summer'):");
        System.out.println("--------------------------------");
        System.out.println(repository.findByUserName("summer"));
        System.out.println("--------------------------------");
        System.out.println("Customers found with findByAddress(\"北京\"):");
        String q="北京";
        for (Customer customer : repository.findByAddress(q)) {
            System.out.println(customer);
        }
    }

    //分页查找
    @Test
    public void fetchPageCustomers() {
        System.out.println("Customers found with fetchPageCustomers:");
        System.out.println("-------------------------------");
        Sort sort = new Sort(Sort.Direction.DESC, "address.keyword");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<Customer> customers=repository.findByAddress("北京", pageable);
        System.out.println("Page customers "+customers.getContent().toString());
    }

    //使用QueryBuilders类查找：QueryBuilders功能非常丰富，可查找API文档使用
    @Test
    public void fetchPage2Customers() {
        System.out.println("Customers found with fetchPageCustomers:");
        System.out.println("-------------------------------");
       QueryBuilder customerQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("address", "北京"));
        Page<Customer> page = repository.search(customerQuery, PageRequest.of(0, 10));
        System.out.println("Page customers "+page.getContent().toString());
        page = repository.search(customerQuery, PageRequest.of(1, 10));
        System.out.println("Page customers "+page.getContent().toString());
    }

    //测试单个匹配
    @Test
    public void testQueryBuilder1(){
        //不分词查询 参数1： 字段名，参数2：字段查询值，因为不分词，所以汉字只能查询一个字，英语是一个单词
        QueryBuilder queryBuilder=QueryBuilders.termQuery("address", "北");
        Iterable<Customer> search1 = repository.search(queryBuilder);
        for (Customer customer : search1){
            System.out.println("测试单个匹配不分词："+customer);
        }

        //分词查询，采用默认的分词器
        QueryBuilder queryBuilder2 = QueryBuilders.matchQuery("address", "北京");
        Iterable<Customer> search2 = repository.search(queryBuilder2);
        for (Customer customer : search2){
            System.out.println("测试单个匹配分词："+customer);
        }

    }

    //测试多个匹配
    @Test
    public void testQueryBuilder2(){
        //不分词查询，参数1：字段名，参数2：多个字段查询值,因为不分词，因此汉字只能查询一个字，英语是一个单词
        QueryBuilder queryBuilder1=QueryBuilders.termsQuery("address", "北","湖");
        Iterable<Customer> search1 = repository.search(queryBuilder1);
        for (Customer customer : search1){
            System.out.println("测试多个匹配不分词："+customer);
        }
        //分词查询，采用默认的分词器
        QueryBuilder queryBuilder2= QueryBuilders.multiMatchQuery("22", "address", "age");
        Iterable<Customer> search2 = repository.search(queryBuilder2);
        for (Customer customer : search2){
            System.out.println("测试多个匹配分词："+customer);
        }
        //匹配所有文件，相当于就没有设置查询条件
        QueryBuilder queryBuilder3=QueryBuilders.matchAllQuery();
        Iterable<Customer> search3 = repository.search(queryBuilder3);
        for (Customer customer : search3){
            System.out.println("测试无条件查询全部文件："+customer);
        }
    }

    //测试模糊查询
    @Test
    public void testQueryBuilder3(){
        //1.常用的字符串查询
        QueryStringQueryBuilder field = QueryBuilders.queryStringQuery("西直").field("address");//左右模糊
        //2.常用的用于推荐相似内容的查询
        MoreLikeThisQueryBuilder moreLikeThisQueryBuilder = QueryBuilders.moreLikeThisQuery(new String[]{"address"},new String[]{"北京"}, null).boost(0.1f);//如果不指定filedName，则默认全部，常用在相似内容的推荐上
        //3.前缀查询，如果字段没分词，就匹配整个字段前缀
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("address", "北");
        //4.fuzzy query:分词模糊查询，通过增加 fuzziness 模糊属性来查询，如能够匹配 address 为 西 前或后加一个字母的文档，fuzziness 的含义是检索的 term 前后增加或减少 n 个单词的匹配查询
        FuzzyQueryBuilder fuzziness = QueryBuilders.fuzzyQuery("address", "安西").fuzziness(Fuzziness.ONE);
        //5.wildcard query:通配符查询，支持* 任意字符串；？任意一个字符
        WildcardQueryBuilder fieldName = QueryBuilders.wildcardQuery("address", "*京*");//前面是fieldname，后面是带匹配字符的字符串
        WildcardQueryBuilder fieldName1 = QueryBuilders.wildcardQuery("address", "北?");
        //输出
        Iterable<Customer> search = repository.search(fieldName1);
        for (Customer customer : search){
            System.out.println("测试输出："+customer);
        }
    }

    @Test
    public void test(){
        //闭区间查询
        QueryBuilder queryBuilder0 = QueryBuilders.rangeQuery("fieldName").from("fieldValue1").to("fieldValue2");
        //开区间查询
        QueryBuilder queryBuilder1 = QueryBuilders.rangeQuery("fieldName").from("fieldValue1").to("fieldValue2").includeUpper(false).includeLower(false);//默认是 true，也就是包含
        //大于
        QueryBuilder queryBuilder2 = QueryBuilders.rangeQuery("fieldName").gt("fieldValue");
        //大于等于
        QueryBuilder queryBuilder3 = QueryBuilders.rangeQuery("fieldName").gte("fieldValue");
        //小于
        QueryBuilder queryBuilder4 = QueryBuilders.rangeQuery("fieldName").lt("fieldValue");
        //小于等于
        QueryBuilder queryBuilder5 = QueryBuilders.rangeQuery("fieldName").lte("fieldValue");
    }

    //聚合查询
    @Test
    public void fetchAggregation() {
        System.out.println("Customers found with fetchAggregation:");
        System.out.println("-------------------------------");

        //使用QueryBuilder构建查询条件
       QueryBuilder customerQuery = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("address", "北京"));
       //使用SumAggregationBuilder指明需要聚合的字段
        SumAggregationBuilder sumBuilder = AggregationBuilders.sum("sumAge").field("age");
        //以前两部分的内容为参数构建成SearchQuery
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(customerQuery)
                .addAggregation(sumBuilder)
                .build();
//        使用Aggregations进行查询
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, new ResultsExtractor<Aggregations>() {
            @Override
            public Aggregations extract(SearchResponse response) {
                return response.getAggregations();
            }
        });

       //转换成map集合
        Map<String, Aggregation> aggregationMap = aggregations.asMap();
        //获得对应的聚合函数的聚合子类，该聚合子类也是个map集合,里面的value就是桶Bucket，我们要获得Bucket
        InternalSum sumAge = (InternalSum) aggregationMap.get("sumAge");
        System.out.println("sum age is "+sumAge.getValue());
    }

}
