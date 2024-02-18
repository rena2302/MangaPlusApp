package object;

public class TruyenTranh {
    private String tenTruyen;
    private String LinkAnh;
    public TruyenTranh (String TenTruyen,String LinkAnhh){
        this.tenTruyen=TenTruyen;
        this.LinkAnh = LinkAnhh;

    }
    public String getTenTruyen(){
        return this.tenTruyen;
    }
    public String setTenTruyen(String tenTruyen){
        return  this.tenTruyen=tenTruyen;
    }
    public String getLinkAnh(){
        return this.LinkAnh;
    }
    public String setLinkAnh(String LinkAnhh){
        return  this.LinkAnh=LinkAnhh;
    }

}
