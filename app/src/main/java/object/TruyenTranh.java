package object;

public class TruyenTranh {
    private String tenTruyen,tenChap,LinkAnh;
    public TruyenTranh (String TenTruyen,String TenChap,String LinkAnhh){
        this.tenTruyen=TenTruyen;
        this.tenChap= TenChap;
        this.LinkAnh = LinkAnhh;

    }
    public String getTenTruyen(){
        return this.tenTruyen;
    }
    public String setTenTruyen(String tenTruyen){
        return  this.tenTruyen=tenTruyen;
    }
    public String getTenChap(){
        return this.tenChap;
    }
    public String setTenChap(String tenChap){
        return this.tenChap=tenChap;
    }
    public String getLinkAnh(){
        return this.LinkAnh;
    }
    public String setLinkAnh(String LinkAnhh){
        return  this.LinkAnh=LinkAnhh;
    }

}
