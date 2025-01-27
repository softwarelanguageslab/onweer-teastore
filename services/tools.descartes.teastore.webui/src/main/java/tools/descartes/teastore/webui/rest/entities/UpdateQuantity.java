package tools.descartes.teastore.webui.rest.entities;

import java.util.Objects;

public class UpdateQuantity {
    private long pid; // product id
    private int quantity;

    public UpdateQuantity() {
    }

    public UpdateQuantity(UpdateQuantity uq) {
        this.setPid(uq.getPid());
        this.setQuantity(uq.getQuantity());
    }

    public long getPid() {
        return this.pid;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateQuantity)) return false;
        UpdateQuantity that = (UpdateQuantity) o;
        return getPid() == that.getPid() && getQuantity() == that.getQuantity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, quantity);
    }
}
