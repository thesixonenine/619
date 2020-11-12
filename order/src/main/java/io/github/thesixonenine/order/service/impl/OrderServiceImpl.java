package io.github.thesixonenine.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.thesixonenine.cart.controller.ICartController;
import io.github.thesixonenine.cart.vo.CartItem;
import io.github.thesixonenine.common.utils.PageUtils;
import io.github.thesixonenine.common.utils.Query;
import io.github.thesixonenine.member.controller.MemberController;
import io.github.thesixonenine.member.controller.MemberReceiveAddressController;
import io.github.thesixonenine.member.entity.MemberReceiveAddressEntity;
import io.github.thesixonenine.order.dao.OrderDao;
import io.github.thesixonenine.order.entity.OrderEntity;
import io.github.thesixonenine.order.interceptor.LoginInterceptor;
import io.github.thesixonenine.order.service.OrderService;
import io.github.thesixonenine.order.vo.CartItemVO;
import io.github.thesixonenine.order.vo.MemberReceiveAddressVO;
import io.github.thesixonenine.order.vo.OrderConfirmVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
    @Autowired
    private MemberReceiveAddressController memberReceiveAddressController;
    @Autowired
    private ICartController cartController;
    @Autowired
    private MemberController memberController;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVO confirmOrder() {
        Long memberId = LoginInterceptor.threadLocal.get().getLeft();
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();
        List<MemberReceiveAddressEntity> addressEntityList = memberReceiveAddressController.getByMemberId(memberId);

        List<MemberReceiveAddressVO> addressList = new ArrayList<>(addressEntityList.size());
        for (MemberReceiveAddressEntity memberReceiveAddressEntity : addressEntityList) {
            MemberReceiveAddressVO vo = new MemberReceiveAddressVO();
            BeanUtils.copyProperties(memberReceiveAddressEntity, vo);
            addressList.add(vo);
        }
        orderConfirmVO.setAddressList(addressList);

        List<CartItem> cartItem = cartController.getCurrentCartItem();

        List<CartItemVO> cartItemList = new ArrayList<>(cartItem.size());
        for (CartItem item : cartItem) {
            CartItemVO vo = new CartItemVO();
            BeanUtils.copyProperties(item, vo);
            cartItemList.add(vo);
        }
        orderConfirmVO.setCartItemList(cartItemList);

        orderConfirmVO.setIntegration(memberController.getById(memberId).getIntegration());
        return orderConfirmVO;
    }

}