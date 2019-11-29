package com.taotao.pojo;

import java.util.Date;
import java.util.List;

import com.taotao.annotation.MaskCodeAnnotation;
import com.taotao.enums.MaskLevelEnum;

public class TbUser extends BaseDto{
    private Long id;
    @MaskCodeAnnotation(maskLevel = MaskLevelEnum.NAMELEVEL)
    private String username;

    private String password;
    @MaskCodeAnnotation(maskLevel = MaskLevelEnum.LEVEL1)
    private String phone;

    private String email;
    @MaskCodeAnnotation(maskLevel = MaskLevelEnum.CARDLEVEL, key = "cardType")
    private String card;
    private String cardType;
    private Date created;

    private Date updated;
    private TbOrder order;
    private List<TbOrder> list;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }


	public List<TbOrder> getList() {
		return list;
	}

	public void setList(List<TbOrder> list) {
		this.list = list;
	}

	public TbOrder getOrder() {
		return order;
	}

	public void setOrder(TbOrder order) {
		this.order = order;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
    
}